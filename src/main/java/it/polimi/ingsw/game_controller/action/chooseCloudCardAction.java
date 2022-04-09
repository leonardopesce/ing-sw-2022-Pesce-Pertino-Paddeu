package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;

public class chooseCloudCardAction extends GameAction{
    private int cloudCardIndex;

    public chooseCloudCardAction(Player player, int cloudCardIndex) {
        super(player);
        this.cloudCardIndex = cloudCardIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.choseCloud(player, cloudCardIndex);
    }
}
