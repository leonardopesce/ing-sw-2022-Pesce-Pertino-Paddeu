package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

public class Centaurus extends AdvancedCharacter{

    public Centaurus(Game game) {
        super(AdvancedCharacterType.CENTAURUS, game);
    }

    /**
     * When resolving a Conquering on an island, Towers do not count towards influence.
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
        return attributes.length==0;
    }
}
