package it.polimi.ingsw.game_model.utils;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.List;

/**
 * A class to manage teacher ownership in the game: if a player has the highest number of students of a specific color
 * on its dining table, player owns the teacher of that specific color
 */
public class CalculatorTeacherOwnership {
    private List<Player> players;

    public CalculatorTeacherOwnership(){}

    /**
     * Given a list of players evaluate the teacher's owner
     * @param player current player
     * @param color teacher to evaluate 's color
     * @param players  Game players list
     */
    public void evaluate(Player player, ColorCharacter color, List<Player> players){
        this.players = players;
        addTeacherToPlayerWithHighestNumberOfStudentOfColor(
                player,
                getPlayerWithTeacherOfColor(color),
                getTeacherOfColorFromPlayerOwner(color)
        );
    }

    /**
     * Applies normalOwnershipCondition method on current player and old owner of the teacher
     * @param currentPlayer player currently playing
     * @param oldOwner player who has the teacher before this method call
     * @param teacher teacher to evaluate the owner
     */
    protected void addTeacherToPlayerWithHighestNumberOfStudentOfColor(Player currentPlayer, Player oldOwner, Teacher teacher){
        normalOwnershipCondition(oldOwner, teacher);
    }

    /**
     * Teacher ownership condition for a player: highest number of students on the teacher's color dining table
     * if old owner still has the teacher ownership condition, keep the teacher on its school
     * If another player has teacher ownership condition, move teacher from old owner's school to new owner school
     * @param oldOwner old owner of the teacher
     * @param teacher teacher to compare
     */
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

    /**
     * Given a teacher compare each player's number of students on the dining table according to the teacher's color,
     * returns the teacher's new owner (the player with most students on the dining table)
     * @param t teacher
     * @return player that is the teacher's new owner
     */
    protected Player getNewPlayerOwnerOfTeacherOfColor(Teacher t){
        return players.stream().reduce((a, b) -> a.getDiningTableWithColor(t.getColor()).getNumberOfStudents() >
                b.getDiningTableWithColor(t.getColor()).getNumberOfStudents() ? a : b).orElse(null);
    }

    /**
     * If the condition of teacher's ownership is true, move a teacher from old owner to new owner,
     * if teacher has no old owner, creates the teacher to give to the new owner.
     * @param color teacher color
     * @return teacher to move, or to create
     */
    protected Teacher getTeacherOfColorFromPlayerOwner(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player.moveTeacherOfColor(color);
            }
        }
        // if no teacher found creates the teacher
        return new Teacher(color);
    }

    /**
     * Given a student color, compare all players and return the player who has the teacher of that color
     * @param color student color to compare
     * @return player with teacher of that color
     */
    protected Player getPlayerWithTeacherOfColor(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player;
            }
        }
        // if no teacher found returns null
        return null;
    }
}
