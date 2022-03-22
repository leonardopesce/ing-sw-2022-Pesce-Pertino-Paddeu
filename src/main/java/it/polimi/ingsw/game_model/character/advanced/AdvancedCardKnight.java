package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.world.Island;

public class AdvancedCardKnight extends AdvancedCharacter{

    public AdvancedCardKnight() {
        super(AdvancedCharacterType.KNIGHT);
    }

    @Override
    public int getCardInfluence(int influence, Player player, Island island){
        return influence + 2;
    }
}
