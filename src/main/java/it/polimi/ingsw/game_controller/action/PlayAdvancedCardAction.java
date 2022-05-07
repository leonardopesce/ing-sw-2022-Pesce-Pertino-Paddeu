package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.network.utils.Logger;

public class PlayAdvancedCardAction extends GameAction{
    private final AdvancedCharacterType card;
    private final Object[] varArgs;

    public PlayAdvancedCardAction(String playerName, AdvancedCharacterType cardSelected, Object... varArgs) {
        super(playerName);
        this.card = cardSelected;
        this.varArgs = varArgs;
    }

    @Override
    public void perform(GameController controller) {
        try {
            controller.playAdvancedCard(playerName, card, varArgs);
        } catch (Exception e) {
            Logger.ERROR("Invalid number or type of arguments for the specified card.", e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Play advanced card action. Played advanced card : " + card.toString();
    }
}
