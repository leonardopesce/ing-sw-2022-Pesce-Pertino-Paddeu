package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.Character;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;

/**
 * An abstract class to represent all the 12 character cards.
 *
 * @see AdvancedCharacterType
 */
public abstract class AdvancedCharacter extends Character {
    private AdvancedCharacterType type;

    public AdvancedCharacter(AdvancedCharacterType type){
        super();
        this.type = type;
    }

    public AdvancedCharacterType getAdvanceCharacterType(){
        return type;
    }

}