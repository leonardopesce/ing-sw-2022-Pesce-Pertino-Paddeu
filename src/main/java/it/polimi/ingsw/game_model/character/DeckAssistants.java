package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;

import java.util.ArrayList;
import java.util.List;

public class DeckAssistants {
    private List<Assistant> assistants;
    private final DeckType type;

    public DeckAssistants(DeckType deckType) {
        this.assistants = new ArrayList<Assistant>();
        this.type = deckType;
        
        for(AssistantType cardType : AssistantType.values()) {
            this.assistants.add(new Assistant(cardType));
        }
    }

    public int getSize(){
        return assistants.size();
    }

    public List<Assistant> getAssistants(){
        return this.assistants;
    }

    public Assistant playAssistant(Assistant x){
        return assistants.remove(assistants.indexOf(x));
    }

    public DeckType getType() {
        return type;
    }
}
