package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.character.Character;

/**
 * A class that model the concept of character in the game: a character is considered a basic character when it has the
 * "color" attribute, its main meaning is to be a father class for more specific classes who use different colors.
 *
 */
public abstract class BasicCharacter<T> extends Character {
    private final T color;

    /**
     * @param color the color of the character. Can be {@link it.polimi.ingsw.game_model.utils.ColorCharacter} or {@link it.polimi.ingsw.game_model.utils.ColorTower}.
     */
    public BasicCharacter(T color) {
        this.color = color;
    }

    /**
     * Returns the color of the character.
     * @return the color of the character.
     *
     * @see it.polimi.ingsw.game_model.utils.ColorCharacter
     * @see it.polimi.ingsw.game_model.utils.ColorTower
     */
    public T getColor() {
        return color;
    }
}
