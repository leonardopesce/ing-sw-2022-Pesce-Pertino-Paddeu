package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

public class MoveStudentToIslandAction extends GameAction{
    private int studentIndex, islandIndex;

    public MoveStudentToIslandAction(String playerName, int studentIndex, int islandIndex) {
        super(playerName);
        this.studentIndex = studentIndex;
        this.islandIndex = islandIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.playerMoveStudentToIsland(playerName, studentIndex, islandIndex);
    }
}
