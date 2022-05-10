package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;

public class Flagman extends AdvancedCharacter{
    public Flagman(Game game){
        super(AdvancedCharacterType.FLAGMAN, game);
    }

    /**
     * Chose an island and calculate the majority as if Mother Nature has stopped her movement there.<br>
     *  In this turn Mother Nature will move as usual and on the island she lands, the majority will normally be calculated.
     *
     * @param attributes a list of parameters to make the card work. In this case it contains the island index selected by the user.
     * @return true if the card got played successfully, false if not.
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)){
            return false;
        }
        Integer islandIdx = (Integer) attributes[0];
        int islandID = game.getTerrain().getIslands().get(islandIdx).getId();

        game.evaluateInfluences(islandID);
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != this.getType().getArgsLength()){
            return false;
        }
        try {
            // If the arg is not an integer, the card is not playable.
            Integer islandIdx = (Integer) attributes[0];

            // If the island index is out of bound, the card is not playable.
            if(islandIdx == null || islandIdx < 0 || islandIdx >= game.getTerrain().getIslands().size()) return false;
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
