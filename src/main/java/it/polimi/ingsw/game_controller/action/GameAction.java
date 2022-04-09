package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;

import java.io.Serializable;

public abstract class GameAction implements Serializable {
    /* public enum gameActionID{
        PLAY_ASSISTANT_CARD_ACTION,
        MOVE_STUDENTS_TO_DINING_HALL_ACTION,
        MOVE_STUDENTS_TO_ISLAND_ACTION,
        MOVE_MOTHER_NATURE_ACTION,
        CHOOSE_CLOUD_CARD_ACTION,
        PLAY_ADVANCED_CARD_ACTION
    } */
    public final Player player;
    /**
        possible action:
        - playAssistantCard
        - moveStudentToDiningHall
        - moveStudentToIsland
        - moveMotherNature
        - chooseCloudIsland
        - playAdvancedCard
            + playBard
            + playBartender
            + playCentaurus
            + playFlagman
            + playHealer
            + playJester
            + playKnight
            + playLandlord
            + playMerchant
            + playMonk
            + playPostman
            + playPrincess
     */

    public GameAction(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void perform(GameController controller);
}
