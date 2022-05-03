package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.utils.ColorCharacter;

/**
 * Student is the class that models the student pieces, it changes its color attribute according to the game rules
 */
public class Student extends BasicCharacter<ColorCharacter> {
    public Student(ColorCharacter color) {
        super(color);
    }
}
