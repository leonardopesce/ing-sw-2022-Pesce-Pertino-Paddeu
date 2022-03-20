package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;


/**
 * A class to represent all the remaining type of Character Card:  they don't need any particular variable.
 *
 * @see AdvancedCharacter
 * @see AdvancedCharacterType
 */
public class NormalAdvancedCharacter extends AdvancedCharacter{

    public NormalAdvancedCharacter(AdvancedCharacterType type){
        super(type);
    }
}
