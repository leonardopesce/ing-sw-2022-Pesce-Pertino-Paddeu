package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_view.board.GameBoard;

public abstract class GameViewClient{
    public static final String ASK_NAME_QUESTION = "Insert a Nickname:";
    public static final String REASK_NAME_QUESTION = "Nickname already chosen. Insert a Nickname:";
    public static final String ASK_DECK_TYPE_QUESTION = "Select your deck, available use number ";
    public static final String ASK_GAME_TYPE_QUESTION = "Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)";
    public static final String ASK_PLAYER_NUMBER_QUESTION = "You are the first player! Choose the number of player in the game (2, 3 or 4)";

    protected Client client;
    protected GameBoard board;
    protected boolean actionSent = false;

    protected GameViewClient(Client client){
        this.client = client;
    }

    public void updateBoardMessage(GameBoard board){
        this.board = board;
        actionSent = false;
        if(board.getCurrentlyPlaying().equals(client.getName())){
            updateBoard(board);
            displayYourTurn();
            if(board.isExpertMode()){
                displayExpertMode();
            }
        }
    }

    public abstract void updateBoard(GameBoard board);
    public abstract void displayYourTurn();
    public abstract void displayExpertMode();
    public abstract void askName();
    public abstract void reaskName();
    public abstract void askDeck(Object decksAvailable);
    public abstract void askGameType();
    public abstract void askPlayerNumber();
    public abstract void gameReady(GameBoard board);
}
