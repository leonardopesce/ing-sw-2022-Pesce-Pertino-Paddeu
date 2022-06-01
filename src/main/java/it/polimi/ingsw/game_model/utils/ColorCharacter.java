package it.polimi.ingsw.game_model.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Object to represent students colors.
 */
public enum ColorCharacter {
        GREEN("green", 0),
        RED("red", 1),
        YELLOW("yellow", 2),
        PINK("pink", 3),
        BLUE("blue", 4);

        private final String colorName;
        private final int tableOrder;

        /**
         * @param colorName the name of the color.
         * @param tableOrder the table index in the {@link it.polimi.ingsw.game_model.school.DiningHall} which has the color with <code>colorName</code>.
         */
        ColorCharacter(String colorName, int tableOrder) {
                this.colorName = colorName;
                this.tableOrder = tableOrder;
        }

        @Override
        public String toString() {
                return colorName;
        }

        /**
         * Returns the index of the table in the {@link it.polimi.ingsw.game_model.school.DiningHall} with the color.
         * @return the index of the table in the {@link it.polimi.ingsw.game_model.school.DiningHall} with the color.
         *
         * @see it.polimi.ingsw.game_model.school.DiningTable
         */
        public int getTableOrder() {
                return tableOrder;
        }

        /**
         * Returns the javafx color corresponding to the <code>ColorCharacter</code> color.
         * @param color the color of which the javafx version is requested.
         * @return the javafx color corresponding to the <code>ColorCharacter</code> color.
         */
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
