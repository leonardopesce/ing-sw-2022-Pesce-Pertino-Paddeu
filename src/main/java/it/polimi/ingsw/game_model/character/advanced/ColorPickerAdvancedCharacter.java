package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

/**
 * A class to represent Landlord and merchant, since they need a color to be picked in order to produce an effect
 */

public class ColorPickerAdvancedCharacter extends AdvancedCharacter{
    ColorCharacter color;

    public ColorPickerAdvancedCharacter(AdvancedCharacterType type) {
        super(type);
    }

    public void setColor(ColorCharacter color) {
        this.color = color;
    }

    public ColorCharacter getColor() {
        return color;
    }
}
