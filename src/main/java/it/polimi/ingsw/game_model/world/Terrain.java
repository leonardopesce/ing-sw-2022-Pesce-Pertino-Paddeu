package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.game_type.GameExpertMode;

import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private final List<CloudCard> cloudCards;
    private final List<AdvancedCharacter> advancedCharacters;
    private final List<Island> islandsRing;
    private static final int MAX_ISLAND_NUMBER = 12;

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
        return islandsRing.get((islandsRing.indexOf(is) - 1) % islandsRing.size());
    }

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


    public void addCloudCard(CloudCard cloudToAdd) {
        this.cloudCards.add(cloudToAdd);
    }

    public Island getIslandWithId(int id) {
        Island is = islandsRing.get(0);
        for(int i = 1; i < islandsRing.size(); i++){
            if(islandsRing.get(i).getId() == id){
                is = islandsRing.get(i);
            }
        }
        return is;
    }

    public void pickAdvancedCard(Game game){
        while(advancedCharacters.size() < GameExpertMode.NUMBER_OF_ADVANCED_CARD){
            AdvancedCharacter character = AdvancedCharacter.getRandomCard(game, advancedCharacters);
            if(advancedCharacters.stream().noneMatch(x -> x.getName().equals(character.getName()))){
                advancedCharacters.add(character);
            }
        }
    }

    public void addStudentToIsland(Student student, int island) {
        islandsRing.get(island).addStudent(student);
    }
}
