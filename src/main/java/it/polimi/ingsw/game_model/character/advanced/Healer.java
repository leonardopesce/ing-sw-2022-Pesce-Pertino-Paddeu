package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Healer extends AdvancedCharacter{
    private int numberOfDeniableIslands;

    public Healer(Game game) {
        super(AdvancedCharacterType.HEALER, game);
        this.numberOfDeniableIslands = 4;
    }



    public void playEffect(Island islandToDeny) {
        if(numberOfDeniableIslands > 0) {
            islandToDeny.denyIsland();
            numberOfDeniableIslands--;
        } else {
            // No bro qui davvero cioè tiriamo fuori una RuntimeException che ti dico io.
        }
    }
}
