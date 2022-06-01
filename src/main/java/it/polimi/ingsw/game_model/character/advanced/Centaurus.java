package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.world.Island;

/**
 * <dt><b>Centaurus</b> - Cost: <b>3</b></dt>
 *      <p>
 *      <dd>
 *           <b>EFFECT</b>: When resolving a Conquering on an island, Towers do not count towards influence.
 *      </dd>
 */
public class Centaurus extends AdvancedCharacter{
    /**
     * @param game the game decorated by this card.
     */
    public Centaurus(Game game) {
        super(AdvancedCharacterType.CENTAURUS, game);
    }

    /**
     * @param attributes  the arguments requested by the character in order to be successfully played. In this case that array must be empty.
     */
    @Override
    public boolean playEffect(Object... attributes) {
        if(!validateArgs(attributes)){
            return false;
        }

        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    @Override
                    public int evaluateForPlayer(Player player, Island island){
                        return playerStudentInfluence(player, island);
                    }
                }
        );
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        return attributes.length==this.getType().getArgsLength();
    }
}
