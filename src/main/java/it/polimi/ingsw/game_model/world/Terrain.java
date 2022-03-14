package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;

import java.util.LinkedList;
import java.util.List;

public class Terrain {
    private List<CloudCard> cloudCards;
    private List<AdvancedCharacter> advancedCharacters;
    private LinkedList<Island> islandsRing;

    public Terrain() {
    }

    public int getNumberOfIsland(){
        return islandsRing.size();
    }

    public List<Island> getIslands(){
        return islandsRing;
    }
}
