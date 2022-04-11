package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeckBoard implements Serializable {
    List<AssistantType> cards = new ArrayList<>();
    AssistantType discardedCard;
    public DeckBoard(Player player) {
        for(Assistant assistant: player.getDeckAssistants().getAssistants()){
            cards.add(assistant.getType());
        }
        discardedCard = player.getDiscardedCard() != null ? player.getDiscardedCard().getType() : null;
    }

    public String print() {
        return cards.toString();
    }

    public List<AssistantType> getCards() {
        return cards;
    }

    public AssistantType getDiscardedCard() {
        return discardedCard;
    }
}
