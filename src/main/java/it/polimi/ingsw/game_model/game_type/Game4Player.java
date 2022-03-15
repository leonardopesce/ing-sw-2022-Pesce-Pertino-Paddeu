package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;

public class Game4Player extends Game{
    
    @Override
    public void updateProfessorOwnershipCondition(Teacher t, DiningTable table, Player pl1, Player pl2) {
        normalUpdateProfessorOwnership(t, table, pl1, pl2);
    }

    @Override
    void refillClouds() {

    }
}
