package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

public class Bard extends AdvancedCharacter{

    public Bard(Game game){
        super(AdvancedCharacterType.BARD, game);
    }


    public void playEffect(Player player, List<Integer> studentsFromEntrance, List<ColorCharacter> studentsFromDiningHall) {
        var playerEntrance = player.getSchool().getEntrance();
        var playerDiningHall = player.getSchool().getDiningHall();
        List<Student> fromEntranceToDiningHall = new ArrayList<>();

        // Removing selected students from entrance and from dining hall and effectively switching them.
        for(Integer i : studentsFromEntrance) fromEntranceToDiningHall.add(playerEntrance.moveStudent(i));
        for(ColorCharacter color : studentsFromDiningHall) {
            playerDiningHall.getTableOfColor(color).removeStudent(1);
            playerEntrance.addStudent(new Student(color));
        }
        for(Student student : fromEntranceToDiningHall) {
            playerDiningHall.getTableOfColor(student.getColor()).addStudent();
        }
    }
}
