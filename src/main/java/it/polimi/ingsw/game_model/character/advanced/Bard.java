package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.school.DiningHall;
import it.polimi.ingsw.game_model.school.Entrance;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

public class Bard extends AdvancedCharacter{

    public Bard(Game game){
        super(AdvancedCharacterType.BARD, game);
    }

    /**
     * You may exchange up to 2 students between your entrance and your dining room.
     * @param player Who has played the
     * @param studentsFromEntrance Student selected from entrance
     * @param studentsFromDiningHall Student selected from dining hall
     */
    public void playEffect(Player player, List<Integer> studentsFromEntrance, List<ColorCharacter> studentsFromDiningHall) {
        Entrance playerEntrance = player.getSchool().getEntrance();
        DiningHall playerDiningHall = player.getSchool().getDiningHall();

        //TODO è necessario controllare che la dimensione dei due array sia uguale?

        // Adding the students to the Dining table (no need to store them they are saved with a counter)
        for(Integer i : studentsFromEntrance) {
            playerDiningHall.getTableOfColor(playerEntrance.moveStudent(i).getColor()).addStudent();
        }
        // Adding the student to the Entrance
        for(ColorCharacter color : studentsFromDiningHall) {
            playerEntrance.addAllStudents(
                    playerDiningHall.getTableOfColor(color).removeStudent(1)
            );
        }

    }
}
