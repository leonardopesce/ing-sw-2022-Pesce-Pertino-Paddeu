package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.world.Island;

public abstract class AdvancedCharacterInfluenceType extends AdvancedCharacter{
    public static final int INFLUENCE_CATEGORY = 0;

    public AdvancedCharacterInfluenceType(AdvancedCharacterType type){
        super(type, INFLUENCE_CATEGORY);
    }

    public abstract int getPlayerInfluence(Player player, Island island);
}
