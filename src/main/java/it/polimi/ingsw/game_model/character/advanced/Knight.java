package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Knight extends AdvancedCharacter{


    public Knight(Game game){
        super(AdvancedCharacterType.KNIGHT, game);
    }

    public void playEffect() {
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    @Override
                    public int evaluate(Player player, Island island){
                        return playerTowerInfluence(player, island) + playerStudentInfluence(player,island) + 2;
                    }
                }
        );
    }
}
