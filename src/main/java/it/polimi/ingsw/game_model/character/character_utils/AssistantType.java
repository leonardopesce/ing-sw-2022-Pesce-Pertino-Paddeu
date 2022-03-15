package it.polimi.ingsw.game_model.character.character_utils;

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
    

    private String name;
    private int cardTurnValue;
    private int possibleSteps;

    AssistantType(String cardName, int cardTurnValue, int cardPossibleSteps) {
        this.name = cardName;
        this.cardTurnValue = cardTurnValue;
        this.possibleSteps = cardPossibleSteps;
    }

    public String getName() {
        return name;
    }

    public int getCardTurnValue() {
        return cardTurnValue;
    }

    public int getPossibleSteps() {
        return possibleSteps;
    }
}
