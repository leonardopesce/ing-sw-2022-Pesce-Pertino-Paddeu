package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Flagman extends AdvancedCharacter{
    public Flagman(Game game){
        super(AdvancedCharacterType.FLAGMAN, game);
    }


    public void playEffect(int islandID){
        game.evaluateInfluences(islandID);
    }
}
