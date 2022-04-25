package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

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
