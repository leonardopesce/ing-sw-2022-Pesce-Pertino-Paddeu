package it.polimi.ingsw.game_model.character.advanced.PropostaDiPaolo;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Healer {
    private final Game game;
    private int numberOfDeniableIslands;

    public Healer(Game game) {
        this.game = game;
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
