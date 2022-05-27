package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;

/**
 * Model the controller action of play the assistant .
 * Every "action" card has an override of the <code>perform</code> method, that do a specific controller action
 * based on the name of the class using the corresponding controller method.
 *
 * @see it.polimi.ingsw.game_model.character.Assistant
 * @see GameController
 */
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

    @Override
    public String toString() {
        return "Play assistant card action. Played assistant card of index: " + assistantIndex;
    }
}
