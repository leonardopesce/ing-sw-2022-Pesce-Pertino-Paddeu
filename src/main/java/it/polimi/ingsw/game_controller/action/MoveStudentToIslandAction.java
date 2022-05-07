package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

public class MoveStudentToIslandAction extends GameAction{
    private final int studentIndex;
    private final int islandIndex;

    public MoveStudentToIslandAction(String playerName, int studentIndex, int islandIndex) {
        super(playerName);
        this.studentIndex = studentIndex;
        this.islandIndex = islandIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.playerMoveStudentToIsland(playerName, studentIndex, islandIndex);
    }

    @Override
    public String toString() {
        return "Move student from dining hall to island action. Student moved from entrance position with id : " + studentIndex + ", to the island with the id : " + islandIndex;
    }
}
