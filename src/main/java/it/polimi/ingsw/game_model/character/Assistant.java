package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assistant)) return false;
        Assistant assistant = (Assistant) o;
        return cardType == assistant.cardType;
    }
}
