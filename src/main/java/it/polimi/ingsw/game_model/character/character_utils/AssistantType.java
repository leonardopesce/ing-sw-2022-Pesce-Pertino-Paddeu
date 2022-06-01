package it.polimi.ingsw.game_model.character.character_utils;

/**
 * Represents all the assistants types.
 */
public enum AssistantType {
    CHEETAH("Ghepardo", 1, 1),
    OSTRICH("Struzzo", 2, 1),
    CAT("Micio", 3, 2),
    EAGLE("Aquila", 4, 2),
    FOX("Volpe", 5, 3),
    SNAKE("Serpente", 6, 3),
    OCTOPUS("Polpo", 7, 4),
    DOG("Cane", 8, 4),
    ELEPHANT("Elefante", 9, 5),
    TURTLE("Tartaruga", 10, 5);
    

    private final String name;
    private final int cardTurnValue;
    private final int possibleSteps;

    /**
     * @param cardName the assistant card name.
     * @param cardTurnValue the assistant card value which determines the order of the action phase.
     * @param cardPossibleSteps the maximum possible steps mother nature can handle in the action phase of this turn.
     */
    AssistantType(String cardName, int cardTurnValue, int cardPossibleSteps) {
        this.name = cardName;
        this.cardTurnValue = cardTurnValue;
        this.possibleSteps = cardPossibleSteps;
    }

    /**
     * Returns the name of the assistant card.
     * @return the name of the assistant card
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the card value which determines the order of the action phase.
     * @return the card value which determines the order of the action phase.
     */
    public int getCardTurnValue() {
        return cardTurnValue;
    }

    /**
     * Returns the maximum possible steps mother nature can handle in the action phase of this turn.
     * @return the maximum possible steps mother nature can handle in the action phase of this turn.
     */
    public int getPossibleSteps() {
        return possibleSteps;
    }
}
