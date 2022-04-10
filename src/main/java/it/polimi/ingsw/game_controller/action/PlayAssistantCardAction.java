package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

public class PlayAssistantCardAction extends GameAction{
    private final int assistantIndex;

    public PlayAssistantCardAction(String playerName, int assistantIndex) {
        super(playerName);
        this.assistantIndex = assistantIndex;
    }

    @Override
    public void perform(GameController controller) {
        controller.selectAssistantCard(playerName, assistantIndex);
    }
}
