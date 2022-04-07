package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;

public abstract class GameView extends Observable<GameAction> implements Observer<MoveMessage> {

    private Player player;

    protected GameView(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    void handleMove(GameAction action) {
        System.out.println("from player: " + player.getNickname() + ", received game action: " + action.toString());
        notify(action);
    }

    public void reportError(String message){
        showMessage(message);
    }

}
