package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

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
