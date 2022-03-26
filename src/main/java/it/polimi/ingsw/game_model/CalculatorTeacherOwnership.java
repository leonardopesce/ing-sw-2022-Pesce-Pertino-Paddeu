package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.List;

public class CalculatorTeacherOwnership {
    private List<Player> players;

    public CalculatorTeacherOwnership(){}

    public void evaluate(Player player, ColorCharacter color, List<Player> players){
        this.players = players;
        addTeacherToPlayerWithHighestNumberOfStudentOfColor(
                player,
                getPlayerWithTeacherOfColor(color),
                getTeacherOfColorFromPlayerOwner(color)
        );
    }

    protected void addTeacherToPlayerWithHighestNumberOfStudentOfColor(Player currentPlayer, Player oldOwner, Teacher teacher){
        normalOwnershipCondition(oldOwner, teacher);
    }

    protected void normalOwnershipCondition(Player oldOwner, Teacher teacher){
        Player newOwner = getNewPlayerOwnerOfTeacherOfColor(teacher);

        if(oldOwner == null || newOwner.getNumberOfStudentAtTableOfColor(teacher.getColor()) >
                oldOwner.getNumberOfStudentAtTableOfColor(teacher.getColor())){
            newOwner.getSchool().addTeacher(teacher);
        }
        else{
            oldOwner.getSchool().addTeacher(teacher);
        }
    }

    protected Player getNewPlayerOwnerOfTeacherOfColor(Teacher t){
        return players.stream().reduce((a, b) -> a.getDiningTableWithColor(t.getColor()).getNumberOfStudents() >
                b.getDiningTableWithColor(t.getColor()).getNumberOfStudents() ? a : b).get();
    }

    private Teacher getTeacherOfColorFromPlayerOwner(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player.moveTeacherOfColor(color);
            }
        }
        // if no teacher found creates the teacher
        return new Teacher(color);
    }

    private Player getPlayerWithTeacherOfColor(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player;
            }
        }
        // if no teacher found returns null
        return null;
    }
}
