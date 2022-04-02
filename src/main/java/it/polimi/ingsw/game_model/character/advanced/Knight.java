package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Knight extends AdvancedCharacter{


    public Knight(Game game){
        super(AdvancedCharacterType.KNIGHT, game);
    }

    /**
     * During the influence calculation this turn, you count as having 2 more influence.
     */
    public void playEffect() {
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    @Override
                    public int evaluateForPlayer(Player player, Island island){
                        if(player.hasPlayedSpecialCard()){
                            return playerTowerInfluence(player, island) + playerStudentInfluence(player,island) + 2;
                        }
                        else {
                            return playerTowerInfluence(player, island) + playerStudentInfluence(player,island);
                        }
                    }
                }
        );
    }
}
