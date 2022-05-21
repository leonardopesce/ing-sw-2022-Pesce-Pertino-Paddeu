package it.polimi.ingsw.game_view;

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
    String FAILED_TO_MOVE_STUDENT_TO_TABLE = "Failed to move the student on that table since it's full.";
    String FAILED_TO_MOVE_STUDENT_TO_TABLE_ERROR = "Table full";
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

    void displayErrorMessage(String errorMsg, String errorType, GameBoard boardToUpdate);
    void updateBoard(GameBoard board);
    void displayYourTurn();
    void displayNotYourTurn();
    void displayOtherPlayerTurn(String otherPlayerName);
    void displayExpertMode();
    void askName();
    void reaskName();
    void displayIsChoosingDeckType(Object playerNameWhoIsChosingTheDeck);
    void askDeck(Object decksAvailable);
    void askGameType();
    void askJoiningAction();
    void displayNoLobbiesAvailable();
    void askLobbyToJoin(Object listOfLobbyInfos);
    void displayLobbyJoined(Object LobbyInfos);
    void askPlayerNumber();
    void gameReady(GameBoard board);
    void reaskAssistant();
    void onPlayerDisconnection(String playerWhoMadeTheLobbyClose);
    GameBoard getBoard();
    void setBoard(GameBoard board);
    Client getClient();
}
