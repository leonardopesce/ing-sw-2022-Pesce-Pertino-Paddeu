package it.polimi.ingsw.game_model.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Object to represent students color
 */
public enum ColorCharacter {
        GREEN("green", 0),
        RED("red", 1),
        YELLOW("yellow", 2),
        PINK("pink", 3),
        BLUE("blue", 4);

        private final String colorName;
        private final int tableOrder;

        ColorCharacter(String colorName, int tableOrder) {
                this.colorName = colorName;
                this.tableOrder = tableOrder;
        }

        @Override
        public String toString() {
                return colorName;
        }

        public int getTableOrder() {
                return tableOrder;
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
