package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

import java.awt.*;

public class Landlord extends AdvancedCharacter{
    public Landlord(Game game){
        super(AdvancedCharacterType.LANDLORD, game);
    }


    public void playEffect(ColorCharacter color){
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    private final ColorCharacter COLOR_TO_EXCLUDE = color;
                    @Override
                    public int evaluateForPlayer(Player player, Island island){
                        return playerTowerInfluence(player, island) + playerStudentInfluenceWithoutColor(player,island,COLOR_TO_EXCLUDE);
                    }
                }
        );
    }
}
