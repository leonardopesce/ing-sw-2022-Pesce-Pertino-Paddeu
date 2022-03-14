package it.polimi.ingsw.game_model.character.basic;

import it.polimi.ingsw.game_model.utils.Color;

public class Tower extends BasicCharacter{
    private Color color;

    public Tower(Color color) {
        super(color);
    }

    public Color getColor() {
        return color;
    }
}
