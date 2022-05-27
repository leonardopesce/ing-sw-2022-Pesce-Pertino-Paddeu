package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

import java.io.Serializable;

/**
 * Abstract class to create the base for controller actions
 */
public abstract class GameAction implements Serializable {
    /**
     * All the available types of action.
     */
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
     * @param playerName the nickname of the player which execute the action.
     */


    public GameAction(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Execute the action by calling the corresponding methods on the controller.
     * @param controller the game controller used to perform the action.
     */
    public abstract void perform(GameController controller);
}
