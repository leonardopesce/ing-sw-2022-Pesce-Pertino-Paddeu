package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

/**
 * Model the controller action of choosing the cloud card
 * Every "action" card has an override of the <code>perform</code> method, that do a specific controller action
 *  based on the name of the class using the corresponding controller method.
 * @see it.polimi.ingsw.game_model.world.CloudCard
 * @see GameController
 */

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

    @Override
    public String toString() {
        return "Choose cloud card action. Chosen card : " + cloudCardIndex;
    }
}
