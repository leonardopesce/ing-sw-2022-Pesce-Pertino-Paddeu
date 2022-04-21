package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_view.board.GameBoard;

public interface GameViewClient{
    String ASK_NAME_QUESTION = "Insert a Nickname:";
    String REASK_NAME_QUESTION = "Nickname already chosen. Insert a Nickname:";
    String ASK_DECK_TYPE_QUESTION = "Select your deck, available use number ";
    String ASK_GAME_TYPE_QUESTION = "Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)";
    String ASK_JOINING_ACTION_QUESTION = "Would you like to create a new match or joining an existing one?\n0. Create a new game\n1. Join a game";
    String ASK_LOBBY_TO_JOIN_QUESTION = "Select the lobby you want to join by entering the lobby owner name:";
    String ASK_PLAYER_NUMBER_QUESTION = "You are the first player! Choose the number of player in the game (2, 3 or 4)";

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

    void displayNotYourTurn();
    void updateBoard(GameBoard board);
    void displayYourTurn();
    void displayExpertMode();
    void askName();
    void reaskName();
    void askDeck(Object decksAvailable);
    void askGameType();
    void askJoiningAction();
    void askLobbyToJoin(Object listOfLobbyInfos);
    void askPlayerNumber();
    void gameReady(GameBoard board);
    void reaskAssistant();
    GameBoard getBoard();
    void setBoard(GameBoard board);
    Client getClient();
}
