package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.world.Island;

public class AdvancedCardCentaurus extends AdvancedCharacterInfluenceType{
    public AdvancedCardCentaurus() {
        super(AdvancedCharacterType.CENTAURUS);
    }

    @Override
    public int getPlayerInfluence(Player player, Island island){
        return new CalculatorInfluence(){
            @Override
            public int evaluate(Player player, Island island){
                return playerStudentInfluence(player, island);
            }
        }.evaluate(player, island);
    }
}
