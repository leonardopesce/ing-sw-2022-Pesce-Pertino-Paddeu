package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.character_utils.PieceColor;

public abstract class basicCharacter extends Character{
    private PieceColor color;

    public basicCharacter(PieceColor color) {
    }

    public PieceColor getColor() {
        return color;
    }
}
