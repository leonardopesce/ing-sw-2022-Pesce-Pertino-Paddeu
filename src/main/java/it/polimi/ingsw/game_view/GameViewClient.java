package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_view.board.GameBoard;

public abstract class GameViewClient extends GameView{
    public static final String ASK_NAME_QUESTION = "Insert a Nickname:";
    public static final String REASK_NAME_QUESTION = "Nickname already chosen. Insert a Nickname:";
    public static final String ASK_DECK_TYPE_QUESTION = "Select your deck, available use number ";
    public static final String ASK_GAME_TYPE_QUESTION = "Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)";
    public static final String ASK_PLAYER_NUMBER_QUESTION = "You are the first player! Choose the number of player in the game (2, 3 or 4)";

    protected Client client;
    protected GameBoard board;

    protected GameViewClient(Client client){
        super("");
        this.client = client;
    }

    public abstract void askName();
    public abstract void reaskName();
    public abstract void askDeck(Object decksAvailable);
    public abstract void askGameType();
    public abstract void askPlayerNumber();
    public abstract void gameReady(GameBoard board);
}
