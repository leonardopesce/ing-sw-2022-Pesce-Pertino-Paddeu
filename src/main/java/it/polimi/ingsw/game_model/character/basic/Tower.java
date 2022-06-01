package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.utils.ColorTower;

/**
 * Tower models the tower piece in the game, it inherits color attribute and changes it according to tower piece color
 * in the game
 */
public class Tower extends BasicCharacter<ColorTower>{
    /**
     * @param color the color of the tower.
     *
     * @see ColorTower
     */
    public Tower(ColorTower color) {
        super(color);
    }

}
