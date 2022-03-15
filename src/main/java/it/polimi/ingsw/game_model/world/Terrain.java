package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.custom_exceptions.IslandNotPresentException;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Terrain {
    private List<CloudCard> cloudCards;
    private List<AdvancedCharacter> advancedCharacters;
    private List<Island> islandsRing;
    private final int MAX_ISLAND_NUMBER = 12;

    public Terrain() {
        cloudCards = new ArrayList<>();
        islandsRing = new ArrayList<>();
        advancedCharacters = new ArrayList<>();

        for(int i = 0; i < MAX_ISLAND_NUMBER; i++){
            islandsRing.add(new Island(i));
        }
    }

    public int getNumberOfIsland(){
        return islandsRing.size();
    }

    public List<Island> getIslands(){
        return islandsRing;
    }

    public Island getNextIsland(Island is) {
        return islandsRing.get((islandsRing.indexOf(is) + 1) % islandsRing.size());
    }

    public Island getPreviousIsland(Island is){
        return islandsRing.get((islandsRing.indexOf(is) - 1) % islandsRing.size());
    }

    public void mergeIsland(Island baseIsland, Island islandToMerge){
        baseIsland.incrementSize();
        baseIsland.addAllStudent(islandToMerge.getStudents());
        baseIsland.addAllTower(islandToMerge.getTowers());
        islandsRing.remove(islandToMerge);
    }
    
    public List<CloudCard> getCloudCards() {
        return this.cloudCards;
    }
    
    public void addCloudCard(CloudCard cloudToAdd) {
        this.cloudCards.add(cloudToAdd);
    }

    public Island getIslandWithId(int id) throws IslandNotPresentException {
        for(Island is: islandsRing){
            if(is.getId() == id){
                return is;
            }
        }
        throw new IslandNotPresentException("The island requested is not available, island ID: " + id);
    }
}
