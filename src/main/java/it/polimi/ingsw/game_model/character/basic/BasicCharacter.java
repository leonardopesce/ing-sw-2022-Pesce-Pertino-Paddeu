package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.character.Character;

/**
 * A class that model the concept of character in the game: a character is considered a basic character when it has the
 * "color" attribute, its main meaning is to be a father class for more specific classes who use different colors
 *
 */
public abstract class BasicCharacter<T> extends Character {
    private final T color;

    public BasicCharacter(T color) {
        this.color = color;
    }

    public T getColor() {
        return color;
    }
}
