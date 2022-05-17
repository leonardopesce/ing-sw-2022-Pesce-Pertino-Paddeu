package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

/**
 * Model controller action of moving students to dining hall
 * Every "action" card has an override of the <code>perform</code> method, that do a specific controller action
 *  based on the name of the class using the corresponding controller method.
 */
public class MoveStudentToDiningHallAction extends GameAction{
    private int studentIndex;

    public MoveStudentToDiningHallAction(String playerName, int studentIndex) {
        super(playerName);
        this.studentIndex = studentIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.playerMoveStudentToDiningHall(playerName, studentIndex);
    }

    @Override
    public String toString() {
        return "Move student to dining hall action. Moved student from entrance position of index : " + studentIndex;
    }
}
