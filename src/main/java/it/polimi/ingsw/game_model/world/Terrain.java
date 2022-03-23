package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.custom_exceptions.IslandNotPresentException;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.StudentStorageAdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;
import it.polimi.ingsw.game_model.game_type.AdvancedGame;
import it.polimi.ingsw.game_model.game_type.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;

public class Terrain {
    private List<CloudCard> cloudCards;
    private List<AdvancedCharacter> advancedCharacters;
    private List<Island> islandsRing;
    private final int MAX_ISLAND_NUMBER = 12;
    private final int DRAW_ADVANCED_CHARACTER = 3;

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

    public void pickAdvancedCard(BagOfStudents bag){
        while(advancedCharacters.size() < AdvancedGame.NUMBER_OF_ADVANCED_CARD){
            AdvancedCharacter character = AdvancedCharacter.getRandomCard();
            if(advancedCharacters.stream().noneMatch(x -> x.getName().equals(character.getName()))){
                advancedCharacters.add(character);
            }
        }
        setUpPickedAdvancedCard(bag);
    }

    private void setUpPickedAdvancedCard(BagOfStudents bag){
        for(AdvancedCharacter character: advancedCharacters){
            if(character.getType().equals(LANDLORD) || character.getType().equals(MERCHANT) ){
                //TODO player chooses a color to assign
            }
            else if(character.getType().equals(MONK) || character.getType().equals(PRINCESS)){
                ((StudentStorageAdvancedCharacter)character).setStudents(bag.drawNStudentFromBag(4));
            }
            else if(character.getType().equals(JESTER)){
                ((StudentStorageAdvancedCharacter)character).setStudents(bag.drawNStudentFromBag(6));
            }
        }
    }

    public void addStudentToIsland(Student student, int island) {
        islandsRing.get(island).addStudent(student);
    }
}
