package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;

public class playAdvancedCardAction extends GameAction{
    private AdvancedCharacter card;
    private Object[] varArgs;

    public playAdvancedCardAction(Player player, AdvancedCharacter card, Object... varArgs) {
        super(player);
        this.card = card;
        this.varArgs = varArgs;
    }

    @Override
    public void perform(GameController controller) {
        try {
            controller.playAdvancedCard(player, card, varArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
