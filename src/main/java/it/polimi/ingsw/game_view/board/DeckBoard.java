package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the cards in the hand of a player in a light way to transmit fewer data as possible
 */
public class DeckBoard implements Serializable {
    public static final long serialVersionUID = 1L;
    private final List<AssistantCardBoard> cards = new ArrayList<>();
    private final AssistantCardBoard discardedCard;
    private final DeckType deckType;

    /**
     * Constructor for the deck board initializes all the card the chosen deck and the last played card
     * @param player of which you need to create the DeckBoard
     * @see Player
     */
    public DeckBoard(Player player) {
        for(Assistant assistant: player.getDeckAssistants().getAssistants()){
            cards.add(new AssistantCardBoard(assistant));
        }
        discardedCard = player.getDiscardedCard() != null ? new AssistantCardBoard(player.getDiscardedCard()) : null;
        deckType = player.getDeckAssistants().getType();
    }

    /**
     * Getter for the card available to play by the corresponding player
     * @return a List containing AssistantCardBoard (all the card not yet played)
     * @see AssistantCardBoard
     */
    public List<AssistantCardBoard> getCards() {
        return cards;
    }

    /**
     * Getter for the last played car
     * @return a AssistantCardBoard representing the last played card from the corresponding player
     * @see AssistantCardBoard
     */
    public AssistantCardBoard getDiscardedCard() {
        return discardedCard;
    }

    /**
     * Getter for the type of deck of the corresponding player
     * @return the DeckType chosen by the player at the beginning of the match
     * @see DeckType
     */
    public DeckType getDeckType() {
        return deckType;
    }

    /**
     * Override of toString to create a method that represent the available card
     * @return a String representing graphically all the available assistant card to play
     */
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
                decks.append(cardsString[i].split("\n")[line].replaceAll("\n", "    ")).append("    ");
            }
            decks.append("\n");
        }
        for(int line = 0; line < cardsString[0].chars().filter(c -> c == '\n').count(); line++){
            for (int i = cardsString.length / 2; i < cardsString.length; i++) {
                decks.append(cardsString[i].split("\n")[line].replaceAll("\n", "    ")).append("    ");
            }
            decks.append("\n");
        }

        return decks.toString();
    }

    /**
     * support function for the method toString
     * @param index int representing the index of the card in the hand of the player
     * @return a String of the single assistant card
     */
    private String getCard(int index){
        AssistantCardBoard card = cards.get(index);
        String name = card.getType().getName() + " (" + index + ")";
        return Printable.TL4_CORNER + Printable.H4_BAR.repeat(15) + Printable.TR4_CORNER + "\n" +
                Printable.V4_BAR + " ".repeat((15 - name.length())/2 + (name.length() % 2 == 0 ? 1 : 0))
                + name + " ".repeat((15 - name.length())/2) + Printable.V4_BAR + "\n" +
                Printable.V4_BAR + "   VALUE: " + card.getType().getCardTurnValue() + ((card.getType().getCardTurnValue() < 10) ? "    " : "   ") + Printable.V4_BAR + "\n" +
                Printable.V4_BAR + "   STEPS: " + card.getMaximumSteps() + "    " + Printable.V4_BAR + "\n" +
                Printable.BL4_CORNER + Printable.H4_BAR.repeat(15) + Printable.BR4_CORNER + "\n";
    }
}
