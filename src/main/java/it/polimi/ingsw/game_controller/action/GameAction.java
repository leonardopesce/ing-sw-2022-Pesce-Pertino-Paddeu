package it.polimi.ingsw.game_controller.action;

import java.io.Serializable;

public abstract class GameAction implements Serializable {
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

    public GameAction(int actionID) {
        this.actionID = actionID;
    }

    public int getActionID(){
        return actionID;
    }


}
