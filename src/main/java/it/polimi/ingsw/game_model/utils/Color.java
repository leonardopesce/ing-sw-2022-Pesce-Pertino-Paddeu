package it.polimi.ingsw.game_model.utils;

import java.util.Arrays;
import java.util.List;

public enum Color {
        BLACK, WHITE, GREY, GREEN, BLUE, RED, PINK, YELLOW;

        public List<Color> getTowerColor(){
                return Arrays.asList(BLACK, WHITE, GREY);
        }

        public List<Color> getPieceColor(){
                return Arrays.asList(GREEN, BLUE, RED, PINK, YELLOW);
        }

}
