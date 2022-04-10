package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;

public class PlayAdvancedCardAction extends GameAction{
    private AdvancedCharacter card;
    private Object[] varArgs;

    public PlayAdvancedCardAction(String playerName, AdvancedCharacter card, Object... varArgs) {
        super(playerName);
        this.card = card;
        this.varArgs = varArgs;
    }

    @Override
    public void perform(GameController controller) {
        try {
            controller.playAdvancedCard(playerName, card, varArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
