package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.util.ArrayList;
import java.util.List;

public class DeckAssistants {
    private List<Assistant> assistants;

    public DeckAssistants() {
        this.assistants = new ArrayList<Assistant>();
        
        for(AssistantType cardType : AssistantType.values()) {
            this.assistants.add(new Assistant(cardType));
        }
    }

    public Assistant getAssistant(int i) throws IndexOutOfBoundsException{
        if(i >= 0 && i < assistants.size()) return this.assistants.get(i);
        else throw new IndexOutOfBoundsException("Index out of bound.");
    }
}
