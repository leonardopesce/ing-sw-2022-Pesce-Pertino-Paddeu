package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_view.board.GameBoard;

public class GameViewGUI extends GameViewClient{

    public GameViewGUI(Client player) {
        super(player);
    }

    @Override
    public void askName() {

    }

    @Override
    public void reaskName() {

    }

    @Override
    public void askDeck(Object availableDecks) {

    }

    @Override
    public void askGameType() {

    }

    @Override
    public void askPlayerNumber() {

    }

    @Override
    public void gameReady(GameBoard board) {

    }

    @Override
    public void update(MoveMessage message) {

    }


    @Override
    protected void showMessage(CommunicationMessage message) {

    }
}
