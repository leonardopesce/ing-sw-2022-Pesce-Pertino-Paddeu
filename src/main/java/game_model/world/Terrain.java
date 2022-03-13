package game_model.world;

import game_model.character.AdvancedCharacter;

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
