package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_view.board.GameBoard;

public abstract class GameViewClient{
    public static final String ASK_NAME_QUESTION = "Insert a Nickname:";
    public static final String REASK_NAME_QUESTION = "Nickname already chosen. Insert a Nickname:";
    public static final String ASK_DECK_TYPE_QUESTION = "Select your deck, available use number ";
    public static final String ASK_GAME_TYPE_QUESTION = "Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)";
    public static final String ASK_JOINING_ACTION_QUESTION = "Would you like to create a new match or joining an existing one?\n0. Create a new game\n1. Join a game";
    public static final String ASK_LOBBY_TO_JOIN_QUESTION = "Select the lobby you want to join by entering the lobby owner name:";
    public static final String ASK_PLAYER_NUMBER_QUESTION = "You are the first player! Choose the number of player in the game (2, 3 or 4)";

    protected enum InputStateMachine {
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

    protected InputStateMachine state;
    protected Client client;
    protected GameBoard board;
    protected boolean actionSent = true;

    protected GameViewClient(Client client){
        this.client = client;
    }

    public void updateBoardMessage(GameBoard board){
        this.board = board;
        if(board.getCurrentlyPlaying().equals(client.getName())){
            updateBoard(board);
            displayYourTurn();
            if(board.isExpertMode()){
                displayExpertMode();
            }
            switch (board.getPhase()){
                case PLANNING_PHASE -> state = InputStateMachine.PLANNING_PHASE_START;
                case ACTION_PHASE_CHOOSING_CLOUD -> state = InputStateMachine.CHOOSE_CLOUD_CARD_START;
                case ACTION_PHASE_MOVING_STUDENTS -> state = InputStateMachine.MOVING_STUDENT_PHASE_START;
                case ACTION_PHASE_MOVING_MOTHER_NATURE -> state = InputStateMachine.MOVE_MOTHER_NATURE_START;
            }
            actionSent = false;
        }
    }

    public abstract void displayNotYourTurn();
    public abstract void updateBoard(GameBoard board);
    public abstract void displayYourTurn();
    public abstract void displayExpertMode();
    public abstract void askName();
    public abstract void reaskName();
    public abstract void askDeck(Object decksAvailable);
    public abstract void askGameType();
    public abstract void askJoiningAction();
    public abstract void askLobbyToJoin(Object listOfLobbyInfos);
    public abstract void askPlayerNumber();
    public abstract void gameReady(GameBoard board);
}
