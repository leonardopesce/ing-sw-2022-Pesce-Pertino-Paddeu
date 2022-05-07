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
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)){
            return false;
        }

        String playerNickname = (String) attributes[0];
        Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);

        player.getDiscardedCard().setPossibleSteps(player.getDiscardedCard().getPossibleSteps() + 2);
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != this.getType().getArgsLength()){
            return false;
        }
        try {
            // If the arg is not a player nickname the card cannot be played.
            String playerNickname = (String) attributes[0];

            // Checking the name
            if(playerNickname == null ||
                    !game.getPlayers().stream().map(Player::getNickname).toList().contains(playerNickname)
            ) return false;
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
