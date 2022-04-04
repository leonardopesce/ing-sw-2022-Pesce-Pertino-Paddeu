package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;

public class Postman extends AdvancedCharacter{
    public Postman(Game game){
        super(AdvancedCharacterType.POSTMAN, game);
    }

    /**
     * You can move Mother Nature up to 2 additional islands compared to the value of the assistant card you played.
     * @param player player that selected the card
     */
    public void playEffect(Player player){
        player.getDiscardedCard().setPossibleSteps(player.getDiscardedCard().getPossibleSteps() + 2);
    }
}
