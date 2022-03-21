package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;

public class Game3Player extends Game{
    private final static int MAX_PLAYERS = 3;
    private final int NUMBER_OF_STUDENTS_ON_CLOUD = 4;

    public Game3Player() {
        super(MAX_PLAYERS);
    }

    @Override
    protected void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1) {
        normalUpdateProfessorOwnership(table1, table2, pl1);
    }

    @Override
    protected int playerInfluence(Player pl, Island island) {
        return playerTowerInfluence(pl, island) + playerStudentInfluence(pl, island);
    }

    @Override
    protected void refillClouds() {
        for(CloudCard cloudCard: terrain.getCloudCards()){
            while(cloudCard.getStudentsOnCloud().size() < NUMBER_OF_STUDENTS_ON_CLOUD){
                cloudCard.getStudentsOnCloud().add(bag.drawStudentFromBag());
            }
        }
    }

    @Override
    protected void playerMoveStudents(Player player){
        int movedStudent = 0;
        while(movedStudent < NUMBER_OF_STUDENTS_ON_CLOUD){
            //TODO aggiungere la parte di spostamento degli studenti (event based)
            movedStudent++;
        }
    }

    @Override
    protected void createCloudCard(){
        terrain.addCloudCard(new CloudCard(NUMBER_OF_STUDENTS_ON_CLOUD));
    }

}
