package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class Flagman extends AdvancedCharacter{
    public Flagman(Game game){
        super(AdvancedCharacterType.FLAGMAN, game);
    }

    /**
     * Chose an island and calculate the majority as if Mother Nature has stopped her movement there.<br>
     *  In this turn Mother Nature will move as usual and on the island she lands, the majority will normally be calculated.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)){
            return false;
        }
        Integer islandID = (Integer) attributes[0];

        game.evaluateInfluences(islandID);
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != this.getType().getArgsLength()){
            return false;
        }
        try {
            Integer islandID = (Integer) attributes[0];
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
