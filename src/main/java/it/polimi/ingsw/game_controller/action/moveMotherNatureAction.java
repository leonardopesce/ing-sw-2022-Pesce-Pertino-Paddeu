package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;

public class moveMotherNatureAction extends GameAction{
    private int steps;

    public moveMotherNatureAction(Player player, int steps) {
        super(player);
        this.steps = steps;
    }

    @Override
    public void perform(GameController controller) {
        controller.moveMotherNatureOfSteps(player, steps);
    }
}
