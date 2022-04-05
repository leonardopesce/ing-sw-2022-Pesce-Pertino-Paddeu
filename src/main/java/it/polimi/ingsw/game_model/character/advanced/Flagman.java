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
     * @param islandID ID of chosen island
     */
    public void playEffect(int islandID){
        game.evaluateInfluences(islandID);
    }
}
