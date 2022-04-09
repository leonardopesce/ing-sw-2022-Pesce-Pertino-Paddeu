package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;

public class PlayAssistantCardAction extends GameAction{

    private final Assistant assistant;

    public PlayAssistantCardAction(Player player, Assistant assistant) {
        super(player);
        this.assistant = assistant;
    }

    @Override
    public void perform(GameController controller) {
        controller.selectAssistantCard(player, assistant);
    }
}
