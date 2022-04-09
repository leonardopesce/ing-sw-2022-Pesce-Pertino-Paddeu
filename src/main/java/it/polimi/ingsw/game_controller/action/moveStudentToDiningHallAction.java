package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;

public class moveStudentToDiningHallAction extends GameAction{
    private int studentIndex;

    public moveStudentToDiningHallAction(Player player, int studentIndex) {
        super(player);
        this.studentIndex = studentIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.playerMoveStudentToDiningHall(player, studentIndex);
    }
}
