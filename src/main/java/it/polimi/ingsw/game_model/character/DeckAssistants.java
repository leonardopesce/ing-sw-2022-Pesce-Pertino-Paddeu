package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;

import java.util.ArrayList;
import java.util.List;

public class DeckAssistants {
    private final List<Assistant> assistants;
    private final DeckType type;

    public DeckAssistants(DeckType deckType) {
        this.assistants = new ArrayList<Assistant>();
        this.type = deckType;
        
        for(AssistantType cardType : AssistantType.values()) {
            this.assistants.add(new Assistant(cardType));
        }
    }

    public Assistant getAssistant(int i) throws IndexOutOfBoundsException{
        if(i >= 0 && i < assistants.size()) return this.assistants.get(i);
        else throw new IndexOutOfBoundsException("Index out of bound.");
    }

    public DeckType getType() {
        return type;
    }
}
