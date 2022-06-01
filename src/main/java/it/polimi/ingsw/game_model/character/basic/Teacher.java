package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.utils.ColorCharacter;

/**
 * Teacher is the class that models the concept of teacher in the game, its possible colors are the same as
 * students one
 */
public class Teacher extends BasicCharacter<ColorCharacter> {
    /**
     * @param color the color of the teacher.
     *
     * @see ColorCharacter
     */
    public Teacher(ColorCharacter color) {
        super(color);
    }

}
