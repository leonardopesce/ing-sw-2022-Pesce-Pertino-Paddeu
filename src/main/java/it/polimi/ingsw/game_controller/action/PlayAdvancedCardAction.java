package it.polimi.ingsw.game_controller.action;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.network.utils.Logger;

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
            Logger.ERROR("Invalid number or type of arguments for the specified card.", e.getMessage());
            //TODO: mandare un messaggio di errore al client per notificare la mancata esecuzione dell'effetto della carta.
        }
    }

    @Override
    public String toString() {
        return "Play advanced card action. Played advanced card : " + card.getType().toString();
    }
}
