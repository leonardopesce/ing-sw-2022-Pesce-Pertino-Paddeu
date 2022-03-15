package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.character.Character;

public abstract class BasicCharacter<T> extends Character {
    public T color;

    public BasicCharacter(T color) {
        this.color = color;
    }

    public T getColor() {
        return color;
    }
}
