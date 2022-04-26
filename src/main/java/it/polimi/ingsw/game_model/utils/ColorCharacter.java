package it.polimi.ingsw.game_model.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum ColorCharacter {
        GREEN("green"),
        BLUE("blue"),
        RED("red"),
        PINK("pink"),
        YELLOW("yellow");

        private String colorName;

        ColorCharacter(String colorName) {
                this.colorName = colorName;
        }

        @Override
        public String toString() {
                return colorName;
        }

        public static Paint getPaint(ColorCharacter color){
                return switch (color){
                        case RED -> Color.RED;
                        case GREEN -> Color.GREEN;
                        case PINK -> Color.PINK;
                        case YELLOW -> Color.YELLOW;
                        case BLUE -> Color.BLUE;
                };
        }
}
