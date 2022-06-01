package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.util.Objects;

/**
 * Assistant models the concept of "assistant card" in the game, it is the card you play at the beginning of your turn,
 * its main attributes are cost and possible steps, in fact when you play an assistant its cost determines players turn's
 * order, and possible steps determines the max number of steps you can move Mother nature on the islands.
 */
public class Assistant extends Character{
    private final AssistantType type;
    private final String name;
    private final int cost;
    private int possibleStep;

    /**
     * @param cardType the type of the assistant card.
     */
    public Assistant(AssistantType cardType) {
        type = cardType;
        name = cardType.getName();
        cost = cardType.getCardTurnValue();
        possibleStep = cardType.getPossibleSteps();
    }

    /**
     * Returns the card value which determines the order of the action phase.
     * @return the card value which determines the order of the action phase.
     */
    public int getValue() {
        return cost;
    }

    /**
     * Returns the maximum possible steps mother nature can handle in the action phase of this turn.
     * @return the maximum possible steps mother nature can handle in the action phase of this turn.
     */
    public int getPossibleSteps() {
        return possibleStep;
    }

    /**
     * Sets the maximum possible steps mother nature can handle in the action phase of this turn.
     * @param possibleStep the maximum possible steps mother nature can handle in the action phase of this turn.
     */
    public void setPossibleSteps(int possibleStep) {
        this.possibleStep = possibleStep;
    }

    /**
     * Returns the assistant card type.
     * @return the assistant card type.
     */
    public AssistantType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return cost == assistant.cost && possibleStep == assistant.possibleStep && type == assistant.type && name.equals(assistant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, cost, possibleStep);
    }
}
