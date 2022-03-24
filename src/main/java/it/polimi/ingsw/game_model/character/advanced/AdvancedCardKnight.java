package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.game_type.GameExpertMode;
import it.polimi.ingsw.game_model.world.Island;

public class AdvancedCardKnight extends AdvancedCharacter{

    public AdvancedCardKnight() {
        super(AdvancedCharacterType.KNIGHT, 0);
    }

    @Override
    public Game playEffect(Game game){
        return new GameExpertMode(game){
            @Override
            protected int playerInfluence(Player player, Island island){
                int influence = new CalculatorInfluence(player, island).evaluate();
                if(player.hasPlayedSpecialCard()){
                    influence += 2;
                }
                return influence;
            }
        };
    }
        /*@Override
    public int getPlayerInfluence(Player player, Island island){

        return new CalculatorInfluence(player, island){
            @Override
            public int evaluate(){
                return playerTowerInfluence() + playerStudentInfluence() + 2;
            }
        }.evaluate();
    }*/
}
