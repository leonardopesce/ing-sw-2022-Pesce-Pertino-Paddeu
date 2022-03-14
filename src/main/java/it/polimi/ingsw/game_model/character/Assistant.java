package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

public class Assistant extends Character{
    // ? - motherNatureTurn sono gli step che può fare madre natura? In tal caso li ho racchiusi in AssistantType.
    // ? - Card superclass ?
    // Ho aggiunto il tipo AssistantType che racchiude le info di una singola carta assistente
    private AssistantType cardType;
    private int value;
    private int motherNatureTurn;

    public Assistant(AssistantType cardType, int value, int motherNatureTurn) {
        this. cardType = cardType;
        this.value = value;
        this.motherNatureTurn = motherNatureTurn;
    }

    public int getValue() {
        return value;
        // return cardType.getCardTurnValue()
    }

    public int getMotherNatureTurn() {
        return motherNatureTurn;
        // return cardType.getPossibleSteps()
    }
}
