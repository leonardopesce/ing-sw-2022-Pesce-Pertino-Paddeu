package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.utils.ColorTower;

/**
 * Tower models the tower piece in the game, it inherits color attribute and changes it according to tower piece color
 * in the game
 */
public class Tower extends BasicCharacter<ColorTower>{

    public Tower(ColorTower color) {
        super(color);
    }

}
