package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.TooManyPlayerException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;

public class Game4Player extends Game{
    private final static int MAX_PLAYERS = 4;
    public final int NUMBER_OF_STUDENTS_ON_CLOUD = 3;

    @Override
    public void addPlayer(Player player) throws TooManyPlayerException {
        if(players.size() < MAX_PLAYERS){
            players.add(player);
            isStartable = players.size() == MAX_PLAYERS;
        }
        else {
            throw new TooManyPlayerException("The game as already reached the limit of " + MAX_PLAYERS + " players");
        }
    }

    @Override
    public void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1) {
        normalUpdateProfessorOwnership(table1, table2, pl1);
    }

    @Override
    public int playerInfluence(Player pl, Island island) {
        return playerTowerInfluence(pl, island) + playerStudentInfluence(pl, island);
    }

    @Override
    public void pickAdvancedCards(){}

    @Override
    public void refillClouds() {
        for(CloudCard cloudCard: terrain.getCloudCards()){
            while(cloudCard.getStudentsOnCloud().size() < NUMBER_OF_STUDENTS_ON_CLOUD){
                cloudCard.getStudentsOnCloud().add(bag.drawStudentFromBag());
            }
        }
    }

    @Override
    public void playerMoveStudents(Player player){
        int movedStudent = 0;
        while(movedStudent < NUMBER_OF_STUDENTS_ON_CLOUD){
            //TODO aggiungere la parte di spostamento degli studenti (event based) nel mezzo potrebbero
            // essere giocate carte avanzate in caso la partita lo permetta
            movedStudent++;
        }
    }

    @Override
    public void createCloudCard(){
        terrain.addCloudCard(new CloudCard(NUMBER_OF_STUDENTS_ON_CLOUD));
    }
}
