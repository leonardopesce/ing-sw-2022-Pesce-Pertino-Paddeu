package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;

public class Postman extends AdvancedCharacter{
    public Postman(Game game){
        super(AdvancedCharacterType.POSTMAN, game);
    }

    public void playEffect(Player player){
        player.getDiscardedCard().incrementPossibleSteps(2);
    }
}
