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
        return new CalculatorInfluence(player, island){
            @Override
            public int evaluate(){
                return playerStudentInfluence();
            }
        }.evaluate();
    }
}
