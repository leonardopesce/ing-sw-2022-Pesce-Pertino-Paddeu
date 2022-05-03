package it.polimi.ingsw.game_model.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Object that represents towers possible colors
 */
public enum ColorTower {
    BLACK, WHITE, GREY;



    public static Paint getPaint(ColorTower color){
        return switch (color){
            case BLACK -> Color.BLACK;
            case GREY -> Color.GREY;
            case WHITE -> Color.WHITE;
        };
    }
}
