package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class GameView extends Observable<GameAction> implements Observer<MoveMessage> {
    private final String playerName;

    public GameView(String playerName) {
        this.playerName = playerName;
    }


    protected String getPlayer(){
        return playerName;
    }

    protected abstract void showMessage(CommunicationMessage message);

    void handleMove(GameAction action) {
        System.out.println("from player: " + ", received game action: " + action.toString());
        notify(action);
    }
}
