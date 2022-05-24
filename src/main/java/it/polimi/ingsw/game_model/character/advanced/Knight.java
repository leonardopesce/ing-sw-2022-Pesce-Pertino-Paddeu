package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.world.Island;

/**
 * <dt><b>Knight</b> - Cost: <b>2</b></dt>
 *       <p>
 *       <dd>
 *           <b>EFFECT</b>: During the influence calculation this turn, you count as having 2 more influence.
 *       </dd>
 */
public class Knight extends AdvancedCharacter{
    public Knight(Game game){
        super(AdvancedCharacterType.KNIGHT, game);
    }

    /**
     * During the influence calculation this turn, you count as having 2 more influence.
     */
    public boolean playEffect(Object... attributes) {
        if(!validateArgs(attributes)){
            return false;
        }

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
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        return attributes.length == this.getType().getArgsLength();
    }
}
