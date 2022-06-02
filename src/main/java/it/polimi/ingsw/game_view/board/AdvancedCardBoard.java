package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.advanced.*;
import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;
import static it.polimi.ingsw.game_view.board.Printable.*;

/**
 *  Class representing an advanced card in a light way to transmit fewer data as possible
 */
public class AdvancedCardBoard implements Serializable {
    public static final long serialVersionUID = 1L;
    private final String name;
    private final AdvancedCharacterType type;
    private final int cost;
    private final List<ColorCharacter> students = new ArrayList<>();
    private int denyCard = 0;

    /**
     * Constructor class, creates the message representation of the advanced card in case of a card  object attach
     * the relative objects
     * @param card the advanced card to initialize
     */
    public AdvancedCardBoard(AdvancedCharacter card) {
        this.name = card.getName();
        this.type = card.getType();
        this.cost = card.getCardCost();
        if(card.getType() == MONK){
            students.addAll(((Monk)card).getStudentsOnCard().stream().map(BasicCharacter::getColor).toList());
        }
        else if(card.getType() == PRINCESS){
            students.addAll(((Princess)card).getStudentsOnCard().stream().map(BasicCharacter::getColor).toList());
        }
        else if(card.getType() == JESTER){
            students.addAll(((Jester)card).getStudentsOnCard().stream().map(BasicCharacter::getColor).toList());
        }
        else if(card.getType() == HEALER){
            denyCard = ((Healer)card).getNumberOfDeniableIslands();
        }
    }

    /**
     * Getter type of card
     * @return type of card
     */
    public AdvancedCharacterType getType() {
        return type;
    }

    /**
     * Getter of students in case the card is a MONK, PRINCESS or JESTER
     * @return a List with the color of the students in case the card is a MONK, PRINCESS or JESTER
     */
    public List<ColorCharacter> getStudents() {
        return students;
    }

    /**
     * Getter the number of students in case the card is a MONK, PRINCESS or JESTER
     * @return an int representing the number of students in case the card is a MONK, PRINCESS or JESTER
     */
    public int getStudentsSize() {
        return students.size();
    }

    /**
     * Getter returns the number of deny available on the HEALER card
     * @return int representing the number of deny available on the HEALER card
     */
    public int getDenyCard() {
        return denyCard;
    }

    /**
     * Getter returns the cost of the card
     * @return an int representing the cost of the card
     */
    public int getCost() {
        return cost;
    }

    /**
     * A method used to represent the card in a String (used to show it on the CLI)
     * @return a String representing graphically the advanced card
     */
    @Override
    public String toString(){
        StringBuilder card = new StringBuilder();
        int line = 0;
        card.append(TL4_CORNER).append(H4_BAR.repeat(11)).append(TR4_CORNER).append("\n")
                .append(V4_BAR).append(" ".repeat((11 - name.length()) / 2)).append(name).append((name.length() < 7 || name.length() % 2 == 0) ? " ".repeat((11 - name.length()) / 2 + 1) : " ".repeat((11 - name.length()) / 2))
                .append(V4_BAR).append("\n")
                .append(V4_BAR).append(" COST:").append(cost).append("    ").append(V4_BAR).append("\n");
        if(name.equals(MONK.getCardName()) || name.equals(PRINCESS.getCardName()) || name.equals(JESTER.getCardName())){
            int i = 0;
            for(ColorCharacter color: students){
                if(i % 2 == 0){
                    card.append(V4_BAR).append(" ");
                }
                card.append(GameBoard.getColorString(color)).append(STUDENT).append(TEXT_RESET).append("    ");
                if(i % 2 == 1){
                    card.append(V4_BAR).append("\n");
                    line++;
                }
                i++;
            }
        }
        else if(name.equals(HEALER.getCardName())){
            card.append(V4_BAR).append("     ").append(DENY).append(denyCard).append(TEXT_RESET).append("    ").append(V4_BAR).append("\n");
            line++;
        }
        while (line < 3){
            card.append(V4_BAR).append("           ").append(V4_BAR).append("\n");
            line++;
        }
        card.append(BL4_CORNER).append(H4_BAR.repeat(11)).append(BR4_CORNER).append("\n");
        card.append(TEXT_YELLOW).append("EFFECT").append(TEXT_RESET).append(": ").append(type.getEffect());

        return card.toString();
    }
}
