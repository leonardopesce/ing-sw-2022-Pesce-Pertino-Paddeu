package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

public class Assistant extends Character{
    private final String name;
    private final int cost;
    private int possibleStep;

    public Assistant(AssistantType cardType) {
        name = cardType.getName();
        cost = cardType.getCardTurnValue();
        possibleStep = cardType.getPossibleSteps();
    }

    public int getValue() {
        return cost;
    }

    public int getPossibleSteps() {
        return possibleStep;
    }

    public void setPossibleSteps(int possibleStep) {
        this.possibleStep = possibleStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assistant assistant)) return false;
        return name.equals(assistant.name) && cost == assistant.cost && possibleStep == assistant.possibleStep;
    }
}
