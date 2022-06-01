package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage assistant distribution to players: each player has the same number of assistants, one for
 * each type, so there are 4 groups of assistants, one for each player's deck.
 */
public class DeckAssistants {
    private final List<Assistant> assistants;
    private final DeckType type;

    /**
     * @param deckType the deck type (magician displayed on the retro of the card).
     *
     * @see DeckType
     */
    public DeckAssistants(DeckType deckType) {
        this.assistants = new ArrayList<>();
        this.type = deckType;
        
        for(AssistantType cardType : AssistantType.values()) {
            this.assistants.add(new Assistant(cardType));
        }
    }

    /**
     * Returns the current size of the deck by counting the Assistant cards which have not been played yet.
     * @return the current size of the deck by counting the Assistant cards which have not been played yet.
     */
    public int getSize(){
        return assistants.size();
    }

    /**
     * Returns the assistants cards which haven't been played yet.
     * @return a list containing the assistants cards which haven't been played yet.
     *
     * @see Assistant
     */
    public List<Assistant> getAssistants(){
        return this.assistants;
    }

    /**
     * Plays the given assistant card, by removing it from the deck of the available cards.
     * @param x the assistant which has been played.
     * @return the assistant removed from the deck of the available cards which has been played.
     *
     * @see Assistant
     */
    public Assistant playAssistant(Assistant x){
        return assistants.remove(assistants.indexOf(x));
    }

    /**
     * Returns the deck type (i.e. the magician displayed on the retro of the card).
     * @return the deck type.
     *
     * @see DeckType
     */
    public DeckType getType() {
        return type;
    }
}
