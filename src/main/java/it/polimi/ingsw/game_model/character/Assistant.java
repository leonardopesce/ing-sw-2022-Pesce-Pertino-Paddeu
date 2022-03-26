package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

public class Assistant extends Character{
    private final AssistantType cardType;

    public Assistant(AssistantType cardType) {
        this.cardType = cardType;
    }

    public int getValue() {
        return cardType.getCardTurnValue();
    }

    public int getPossibleSteps() {
        return cardType.getPossibleSteps();
    }

    public void incrementPossibleSteps(int n) {
        this.cardType.setPossibleSteps(this.getPossibleSteps()+n);
    }
}
