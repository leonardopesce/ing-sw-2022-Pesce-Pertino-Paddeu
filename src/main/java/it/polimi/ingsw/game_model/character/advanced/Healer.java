package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Healer extends AdvancedCharacter{
    private int numberOfDeniableIslands = 4;

    public Healer(Game game) {
        super(AdvancedCharacterType.HEALER, game);
    }

    public void playEffect(Island islandToDeny) {
        if(numberOfDeniableIslands > 0) {
            islandToDeny.denyIsland();
            numberOfDeniableIslands--;
        }
    }
}
