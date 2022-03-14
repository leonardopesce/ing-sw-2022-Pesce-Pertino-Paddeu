package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.character.Character;
import it.polimi.ingsw.game_model.utils.Color;

public abstract class BasicCharacter extends Character {
    public Color color;

    public BasicCharacter(Color color) {
    }

    public Color getColor() {
        return color;
    }
}
