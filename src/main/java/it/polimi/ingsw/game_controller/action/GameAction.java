package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;

import java.io.Serializable;

public abstract class GameAction implements Serializable {
    public static final int PLAY_ASSISTANT_ID = 0;
    public final Player player;
    public final int actionID;
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

    public GameAction(Player player, int actionID) {
        this.player = player;
        this.actionID = actionID;
    }

    public int getActionID(){
        return actionID;
    }

    public Player getPlayer() {
        return player;
    }

    public static GameAction createGameActionFromID(Player player, Assistant assistant){
        return new PlayAssistantCardAction(player, assistant);
    }


    public abstract void perform(GameController controller);
}
