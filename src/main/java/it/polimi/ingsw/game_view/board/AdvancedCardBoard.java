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

public class AdvancedCardBoard implements Serializable {
    private final String name;
    private final AdvancedCharacterType type;
    private final int cost;
    private final List<ColorCharacter> students = new ArrayList();
    private int denyCard = 0;


    public AdvancedCardBoard(AdvancedCharacter card) {
        this.name = card.getName();
        this.type = card.getType();
        this.cost = card.getType().getCardCost();
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

    public AdvancedCharacterType getType() {
        return type;
    }

    @Override
    public String toString(){
        StringBuilder card = new StringBuilder();
        int line = 0;
        card.append(TL4_CORNER).append(H4_BAR.repeat(11)).append(TR4_CORNER).append("\n")
                .append(V4_BAR).append(" ".repeat((11 - name.length()) / 2)).append(name).append(" ".repeat((11 - name.length()) / 2 + 1))
                .append(V4_BAR).append("\n")
                .append(V4_BAR).append("\tCOST:").append(cost).append("\t").append(V4_BAR).append("\n");
        if(name.equals(MONK.getCardName()) || name.equals(PRINCESS.getCardName()) || name.equals(JESTER.getCardName())){
            int i = 0;
            for(ColorCharacter color: students){
                if(i % 2 == 0){
                    card.append(V4_BAR).append("\t");
                }
                card.append(GameBoard.getColorString(color)).append(STUDENT).append(TEXT_RESET).append("\t");
                if(i % 2 == 1){
                    card.append(V4_BAR).append("\n");
                    line++;
                }
                i++;
            }
        }
        else if(name.equals(HEALER.getCardName())){
            card.append(V4_BAR).append("\t").append(DENY).append(denyCard).append(TEXT_RESET).append("\t\t").append(V4_BAR).append("\n");
            line++;
        }
        while (line < 3){
            card.append(V4_BAR).append("\t\t\t").append(V4_BAR).append("\n");
            line++;
        }
        card.append(BL4_CORNER).append(H4_BAR.repeat(11)).append(BR4_CORNER).append("\n");

        return card.toString();
    }
}
