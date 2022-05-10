package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;

import java.util.ArrayList;
import java.util.List;

/**
 * An object to represent the game terrain, a terrain is made of islands, cloud cards and advanced characters
 */
public class Terrain {
    private final List<CloudCard> cloudCards;
    private final List<AdvancedCharacter> advancedCharacters;
    private final List<Island> islandsRing;
    private static final int MAX_ISLAND_NUMBER = 12;

    /**
     * Add islands to build the island ring
     */
    public Terrain() {
        cloudCards = new ArrayList<>();
        islandsRing = new ArrayList<>();
        advancedCharacters = new ArrayList<>();

        for(int i = 0; i < Terrain.MAX_ISLAND_NUMBER; i++){
            islandsRing.add(new Island(i));
        }
    }

    public List<Island> getIslands(){
        return islandsRing;
    }

    public Island getNextIsland(Island is) {
        return islandsRing.get((islandsRing.indexOf(is) + 1) % islandsRing.size());
    }

    public Island getPreviousIsland(Island is){
        return islandsRing.get(Math.floorMod((islandsRing.indexOf(is) - 1), islandsRing.size()));
    }

    /**
     * Manage the merge of island: islands merge if each island has built a tower of the same color
     * @param baseIsland base island
     * @param islandToMerge island to merge
     */
    public void mergeIsland(Island baseIsland, Island islandToMerge){
        baseIsland.incrementSize();
        baseIsland.addAllStudent(islandToMerge.getStudents());
        baseIsland.addAllTower(islandToMerge.getTowers());
        while(islandToMerge.isBlocked()){
            baseIsland.denyIsland();
            islandToMerge.freeIsland();
        }
        islandsRing.remove(islandToMerge);
    }
    
    public List<CloudCard> getCloudCards() {
        return this.cloudCards;
    }


    /**
     * Add a cloud card on the game terrain
     * @param cloudToAdd
     */
    public void addCloudCard(CloudCard cloudToAdd) {
        this.cloudCards.add(cloudToAdd);
    }

    /**
     * Give each island an id, each island has unique id
     * @param id
     * @return island with id
     */
    public Island getIslandWithId(int id) {
        Island is = islandsRing.get(0);
        for(int i = 1; i < islandsRing.size(); i++){
            if(islandsRing.get(i).getId() == id){
                is = islandsRing.get(i);
            }
        }
        return is;
    }

    /**
     * In expert mode, give an advanced card to a player, each player has different advanced cards
     * @param game
     */
    public void pickAdvancedCard(Game game){
        while(advancedCharacters.size() < GameExpertMode.NUMBER_OF_ADVANCED_CARD){
            AdvancedCharacter character = AdvancedCharacter.getRandomCard(game, advancedCharacters);
            if(advancedCharacters.stream().noneMatch(x -> x.getName().equals(character.getName()))){
                advancedCharacters.add(character);
            }
        }
    }

    public List<AdvancedCharacter> getAdvancedCharacters() {
        return advancedCharacters;
    }

    public void addStudentToIsland(Student student, int island) {
        islandsRing.get(island).addStudent(student);
    }
}
