package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeckBoard implements Serializable {
    private final List<AssistantType> cards = new ArrayList<>();
    private final AssistantType discardedCard;
    private final DeckType deckType;

    public DeckBoard(Player player) {
        for(Assistant assistant: player.getDeckAssistants().getAssistants()){
            cards.add(assistant.getType());
        }
        discardedCard = player.getDiscardedCard() != null ? player.getDiscardedCard().getType() : null;
        deckType = player.getDeckAssistants().getType();
    }

    public List<AssistantType> getCards() {
        return cards;
    }

    public AssistantType getDiscardedCard() {
        return discardedCard;
    }

    public DeckType getDeckType() {
        return deckType;
    }

    @Override
    public String toString() {
        StringBuilder decks = new StringBuilder();
        decks.append("ASSISTANT CARDS\n");
        String[] cardsString = new String[cards.size()];
        for(int i = 0; i < cards.size(); i++){
            cardsString[i] = getCard(i);
        }
        for(int line = 0; line < cardsString[0].chars().filter(c -> c == '\n').count(); line++){
            for (int i = 0; i < cardsString.length / 2; i++) {
                decks.append(cardsString[i].split("\n")[line].replaceAll("\n", "\t")).append("\t");
            }
            decks.append("\n");
        }
        for(int line = 0; line < cardsString[0].chars().filter(c -> c == '\n').count(); line++){
            for (int i = cardsString.length / 2; i < cardsString.length; i++) {
                decks.append(cardsString[i].split("\n")[line].replaceAll("\n", "\t")).append("\t");
            }
            decks.append("\n");
        }

        return decks.toString();
    }

    private String getCard(int index){
        AssistantType card = cards.get(index);
        String name = card.getName() + " (" + index + ")";
        return Printable.TL4_CORNER + Printable.H4_BAR.repeat(15) + Printable.TR4_CORNER + "\n" +
                Printable.V4_BAR + " ".repeat((15 - name.length())/2 + (name.length() % 2 == 0 ? 1 : 0))
                + name + " ".repeat((15 - name.length())/2) + Printable.V4_BAR + "\n" +
                Printable.V4_BAR + "\tVALUE: " + card.getCardTurnValue() + "\t" + Printable.V4_BAR + "\n" +
                Printable.V4_BAR + "\tSTEPS: " + card.getPossibleSteps() + "\t" + Printable.V4_BAR + "\n" +
                Printable.BL4_CORNER + Printable.H4_BAR.repeat(15) + Printable.BR4_CORNER + "\n";
    }
}
