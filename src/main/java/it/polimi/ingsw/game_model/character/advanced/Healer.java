package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.world.Island;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *    <dt><b>Healer</b> - Cost: <b>2</b></dt>
 *       <p>
 *       <dd>
 *           At the beginning of the match put the 4 No Entry tiles on this card.<br>
 *          <b>EFFECT</b>: Place a No Entry tile on an island of your choice. The first time Mother Nature ends her movement there put the no entry tile back onto this card without calculating influence or placing any tower.
 *      </dd>
 */
public class Healer extends AdvancedCharacter{
    private int numberOfDeniableIslands = 4;


    public Healer(Game game) {
        super(AdvancedCharacterType.HEALER, game);
    }

    /**
     * Place a No Entry tile on an island of your choice. The first time Mother Nature ends her movement
     * there put the no entry tile back onto this card without calculating influence of placing any tower.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes) {
        if(!validateArgs(attributes)){
            return false;
        }
        Integer islandIdx = (Integer) attributes[0];
        Island islandToDeny = game.getTerrain().getIslands().get(islandIdx);

        islandToDeny.denyIsland();
        numberOfDeniableIslands--;

        islandToDeny.getIsBlocked().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if((Integer) newValue < (Integer) oldValue) {
                    numberOfDeniableIslands++;
                    islandToDeny.getIsBlocked().removeListener(this);
                }
            }
        });

        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != this.getType().getArgsLength()){
            return false;
        }
        try {
            // If the arg passed is not an island index (integer) the card cannot be played.
            Integer islandIdx = (Integer) attributes[0];

            // If the card has not enough deny-tiles on it, it cannot be played.
            if(numberOfDeniableIslands == 0) return false;

            //If the island idx is out of bound the card is not playable.
            if(islandIdx == null || islandIdx < 0 || islandIdx >= game.getTerrain().getIslands().size()) return false;
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public int getNumberOfDeniableIslands() {
        return numberOfDeniableIslands;
    }
}
