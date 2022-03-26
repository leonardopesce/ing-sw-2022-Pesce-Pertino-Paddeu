package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.List;

public class CalculatorProfessorOwnership {
    private final List<Player> players;
    private final Player player;

    public CalculatorProfessorOwnership(List<Player> players, Player player){
        this.players = players;
        this.player = player;
    }



    protected final void normalUpdateProfessorOwnership(DiningTable table1, DiningTable table2, Player pl1){
        if (table1.getNumberOfStudents() > table2.getNumberOfStudents()) {
            pl1.getSchool().addTeacher(getTeacherOfColorFromAllPlayers(table1.getColor()));
        }
    }

    /**
     * Given the player who as just moved is students moves the controls that the professors are in the right schools
     *
     * @param pl1 the player that has just moved his students
     *
     * @see Player
     */
    public void updateProfessorsOwnership(Player pl1) {
        // for the dining table of pl1
        for (DiningTable table : pl1.getSchool().getDiningHall().getTables()) {
            // search in all players except pl1
            for (Player pl2 : players.stream().filter(player -> !player.getNickname().equals(pl1.getNickname())).toList()) {
                //Search in all the teacher of players pl2
                updateProfessorOwnershipCondition(table, pl2.getDiningTableWithColor(table.getColor()), pl1);
            }
        }
    }

    protected void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1) {
        normalUpdateProfessorOwnership(table1, table2, pl1);
    }

    protected Teacher getTeacherOfColorFromAllPlayers(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player.getTeacherOfColor(color);
            }
        }
        // if no player has the teacher you are the first one to get it
        return new Teacher(color);
    }
}
