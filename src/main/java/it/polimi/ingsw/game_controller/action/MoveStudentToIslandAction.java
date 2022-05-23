package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

/**
 * Model the controller action of moving students to islands
 * Every "action" card has an override of the <code>perform</code> method, that do a specific controller action
 *  based on the name of the class using the corresponding controller method.
 * @see it.polimi.ingsw.game_model.character.basic.Student
 * @see it.polimi.ingsw.game_model.world.Island
 * @see GameController
 */
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
        controller.playerMoveStudentToIsland(playerName, studentIndex, controller.getIslandIndexFromID(islandIndex));
    }

    @Override
    public String toString() {
        return "Move student from dining hall to island action. Student moved from entrance position with id : " + studentIndex + ", to the island with the id : " + islandIndex;
    }
}
