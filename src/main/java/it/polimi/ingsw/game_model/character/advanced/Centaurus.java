package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Centaurus extends AdvancedCharacter{

    public Centaurus(Game game) {
        super(AdvancedCharacterType.CENTAURUS, game);
    }
    public void playEffect() {
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    @Override
                    public int evaluateForPlayer(Player player, Island island){
                        if(player.hasPlayedSpecialCard()) {
                            return playerStudentInfluence(player, island);
                        } else {
                            return playerTowerInfluence(player, island) + playerStudentInfluence(player,island);
                        }
                    }
                }
        );
    }
}
