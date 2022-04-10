package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

public class ChooseCloudCardAction extends GameAction{
    private int cloudCardIndex;

    public ChooseCloudCardAction(String playerName, int cloudCardIndex) {
        super(playerName);
        this.cloudCardIndex = cloudCardIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.choseCloud(playerName, cloudCardIndex);
    }
}
