package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.world.Island;

public interface ExpertMode {
    Assistant playedCard = null;

    void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1);
    int playerInfluence(Player pl, Island island);
}
