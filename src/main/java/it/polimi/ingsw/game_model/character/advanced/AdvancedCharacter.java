package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.Character;

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