package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

/**
 * Model the action to move mother nature
 * Every "action" card has an override of the <code>perform</code> method, that do a specific controller action
 *  based on the name of the class using the corresponding controller method.
 **/
public class MoveMotherNatureAction extends GameAction{
    private int steps;

    public MoveMotherNatureAction(String playerName, int steps) {
        super(playerName);
        this.steps = steps;
    }

    @Override
    public void perform(GameController controller) {
        controller.moveMotherNatureOfSteps(playerName, steps);
    }

    @Override
    public String toString() {
        return "Move mother nature action of steps : " + steps;
    }
}
