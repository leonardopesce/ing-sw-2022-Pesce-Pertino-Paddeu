package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.character.Character;

public abstract class BasicCharacter<T> extends Character {
    private final T color;

    public BasicCharacter(T color) {
        this.color = color;
    }

    public T getColor() {
        return color;
    }
}
