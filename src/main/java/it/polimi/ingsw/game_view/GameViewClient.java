package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.game_view.board.GameBoard;

public interface GameViewClient{
    String ASK_NAME_QUESTION = "Insert a Nickname:";
    String REASK_NAME_QUESTION = "Nickname already chosen. Insert a Nickname:";
    String ASK_DECK_TYPE_QUESTION = "Select your deck, available use number ";
    String ASK_GAME_TYPE_QUESTION = "Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)";
    String ASK_JOINING_ACTION_QUESTION = "Would you like to create a new match or joining an existing one?\n0. Create a new game\n1. Join a game";
    String ASK_LOBBY_TO_JOIN_QUESTION = "Select the lobby you want to join by entering the lobby owner name:";
    String ASK_PLAYER_NUMBER_QUESTION = "You are the first player! Choose the number of player in the game (2, 3 or 4)";
    String FAILED_TO_MOVE_STUDENT = "Failed to move the student you selected. The table is probably full or you selected an invalid entrance position.";
    String FAILED_TO_MOVE_STUDENT_ERROR = "Invalid student move";
    String FAILED_TO_MOVE_MOTHER_NATURE = "Failed to move mother nature of the given steps since they are too many.";
    String FAILED_TO_MOVE_MOTHER_NATURE_ERROR = "Too many steps";
    String INVALID_CLOUD_CHOSEN = "You cannot pick up that cloud since it's empty.";
    String INVALID_CLOUD_CHOSEN_ERROR = "Empty cloud chosen";
    String INVALID_ACTION = "The move you made is playable only in the correct action phase";
    String INVALID_ACTION_ERROR = "Action Phase only";
    String ADVANCED_CARD_NOT_PLAYABLE = "The advanced card is not playable with the given params.";
    String ADVANCED_CARD_NOT_PLAYABLE_ERROR = "Invalid params";
    String ADVANCED_CARD_ALREADY_PLAYED_IN_TURN = "You have already played an advanced card this turn.";
    String ADVANCED_CARD_ALREADY_PLAYED_IN_TURN_ERROR = "One advanced card per turn";
    String NOT_EXPERT_MODE_GAME = "The game is not in expert mode, so you cannot play any character card.";
    String NOT_EXPERT_MODE_GAME_ERROR = "Normal game, not advanced";
    String NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD = "You haven't got enough money to play that card.";
    String NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD_ERROR = "Not enough money";

    /**
     * Class representing all the possible states of the FSM which handles the game phase client-side.
     *
     * <p>
     *     Follows a brief description of each state:
     *     <ul>
     *         <li><b>PLANNING_PHASE_START</b>: before choosing the assistant card;</li>
     *         <li><b>SELECT_ASSISTANT_CARD_SEND_MESSAGE</b>: after choosing the assistant card;</li>
     *         <li><b>MOVING_STUDENT_PHASE_START</b>: before choosing the student to move from the entrance;</li>
     *         <li><b>MOVE_STUDENT_SECOND_PHASE</b>: after choosing the student to move from the entrance but before chosing where to place it (island vs dining hall);</li>
     *         <li><b>MOVE_STUDENT_SEND_MESSAGE</b>: after choosing the student to move from the entrance and where to place it;</li>
     *         <li><b>MOVE_MOTHER_NATURE_START</b>: before choosing how many steps mother nature has to do;</li>
     *         <li><b>MOVE_MOTHER_NATURE_SEND_MESSAGE</b>: after choosing how many steps mother nature has to do;</li>
     *         <li><b>CHOOSE_CLOUD_CARD_START</b>: before choosing the cloud card from which the students have to be picked;</li>
     *         <li><b>CHOOSE_CLOUD_CARD_SEND_MESSAGE</b>: after choosing the cloud card from which the students have to be picked;</li>
     *         <li><b>PLAY_ADVANCED_CARD</b>: while playing an advanced card.</li>
     *     </ul>
     * </p>
     */
    enum InputStateMachine {
        PLANNING_PHASE_START,
        SELECT_ASSISTANT_CARD_SEND_MESSAGE,
        MOVING_STUDENT_PHASE_START,
        MOVE_STUDENT_SECOND_PHASE,
        MOVE_STUDENT_SEND_MESSAGE,
        MOVE_MOTHER_NATURE_START,
        MOVE_MOTHER_NATURE_SEND_MESSAGE,
        CHOOSE_CLOUD_CARD_START,
        CHOOSE_CLOUD_CARD_SEND_MESSAGE,
        PLAY_ADVANCED_CARD
    }

    /**
     * Display error message in game board
     * @param errorMsg string containing the error message
     * @param errorType String containing the type of error
     * @param boardToUpdate the Game board to get the player at which the error message is showed
     */
    void displayErrorMessage(String errorMsg, String errorType, GameBoard boardToUpdate);

    /**
     * Updates the entire board in the UI.
     * @param board the board containing the update
     */
    void updateBoard(GameBoard board);

    /**
     * Displays the user that it is his turn.
     */
    void displayYourTurn();

    /**
     * Displays on the game, 'not your turn' string.
     */
    void displayNotYourTurn();

    /**
     * Displays a message indicating that it is another player turn, by specifying the nickname of the player who is playing.
     * @param otherPlayerName the nickname of the player who is playing.
     */
    void displayOtherPlayerTurn(String otherPlayerName);

    /**
     * Displays that the game is played in expert mode.
     */
    void displayExpertMode();

    /**
     * Make the client choose his nickname.
     */
    void askName();

    /**
     * In case of errors while picking the name (nickname already taken or invalid nickname chosen), makes the client
     * choose again the nickname.
     */
    void reaskName();

    /**
     * Displays the player who is currently choosing the deck
     * @param playerNameWhoIsChosingTheDeck the name of the player which is choosing the deck
     */
    void displayIsChoosingDeckType(Object playerNameWhoIsChosingTheDeck);

    /**
     * Updates the UI with the available decks the user can choose.
     * @param availableDecks the available decks.
     */
    void askDeck(Object availableDecks);

    /**
     * Updates the UI to let the user choose the game type.
     */
    void askGameType();

    /**
     * Makes the user choose what to do when he connects to the server, whether to create a new game or joining an
     * existing one.
     */
    void askJoiningAction();

    /**
     * Display an error message when a player wants to join a lobby but no lobbies have been created yet.
     */
    void displayNoLobbiesAvailable();

    /**
     * Makes the user choose which lobby to join by listing all the possible ones with their respective information.
     * @param listOfLobbyInfos list of lobbies infos of the lobbies currently open in the server.
     */
    void askLobbyToJoin(Object listOfLobbyInfos);

    /**
     * Displays the joined lobby and the players in it.
     * @param lobbyInfos info about the lobby.
     */
    void displayLobbyJoined(Object lobbyInfos);

    /**
     * Makes the user choose the number of players during the game creation, by setting up properly the UI.
     */
    void askPlayerNumber();

    /**
     * Function called when the game is ready to start. The board is shown and the match begins.
     * @param board first board update received
     */
    void gameReady(GameBoard board);

    /**
     * Notifies the user that the assistant played was already played by someone else in the same turn and asks him
     * to play another one.
     */
    void reaskAssistant();

    /**
     * Function called when a player disconnects from the lobby. The client's app gets closed by displaying an error message.
     * @param playerWhoMadeTheLobbyClose the name of the player that disconnected and so killed the lobby
     */
    void onPlayerDisconnection(String playerWhoMadeTheLobbyClose);

    /**
     * Returns the current game board.
     * @return the current game board.
     */
    GameBoard getBoard();

    /**
     * Given a new board, coming from the server, sets it as the current board.
     * @param board the new board to set up.
     *
     * @see GameBoard
     */
    void setBoard(GameBoard board);

    /**
     * Returns the client associated to this interface.
     * @return the client associated to this interface.
     *
     * @see Client
     */
    Client getClient();

    /**
     * Displays the end of the game.
     * @param condition end game condition (could be WIN, LOSE, DRAW).
     */
    void displayEndGame(CommunicationMessage.MessageType condition);
}
