package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

import java.io.Serializable;

public abstract class GameAction implements Serializable {
    public enum gameActionID{
        PLAY_ASSISTANT_CARD_ACTION,
        MOVE_STUDENTS_TO_DINING_HALL_ACTION,
        MOVE_STUDENTS_TO_ISLAND_ACTION,
        MOVE_MOTHER_NATURE_ACTION,
        CHOOSE_CLOUD_CARD_ACTION,
        PLAY_ADVANCED_CARD_ACTION
    }
    public final String playerName;
    /**
        possible action:
        - playAssistantCard
        - moveStudentToDiningHall
        - moveStudentToIsland
        - moveMotherNature
        - chooseCloudIsland
        - playAdvancedCard
     * @param playerName
     */


    public GameAction(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Abstract class to create the base for controller actions
     * @param controller
     */
    public abstract void perform(GameController controller);
}
