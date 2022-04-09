package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;

public class moveStudentToIslandAction extends GameAction{
    private int studentIndex, islandIndex;

    public moveStudentToIslandAction(Player player, int studentIndex, int islandIndex) {
        super(player);
        this.studentIndex = studentIndex;
        this.islandIndex = islandIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.playerMoveStudentToIsland(player, studentIndex, islandIndex);
    }
}
