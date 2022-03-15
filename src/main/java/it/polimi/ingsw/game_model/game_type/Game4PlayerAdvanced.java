package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.world.Terrain;

public class Game4PlayerAdvanced extends Game4Player implements ExpertMode{
    AdvancedCharacter playerCard; //TODO this card will be assigned if a card is played

    public void updateProfessorOwnershipCondition(Teacher t, DiningTable table, Player pl1, Player pl2) {
        switch(playerCard.getAdvanceCharacterType()){
            case CENTAURUS:
                if (t.getColor() == table.getColor() &&
                        table.getNumberOfStudents() >= pl2.getDiningTableWithColor(table.getColor()).getNumberOfStudents()) {
                    pl1.getSchool().addTeacher(t);
                    pl2.getTeachers().remove(t);
                }
                break;

            default:
                normalUpdateProfessorOwnership(t, table, pl1, pl2);
                break;
        }
    }

    @Override
    public void evaluateInfluences(Terrain terrain, MotherNature motherNature) {

    }

}