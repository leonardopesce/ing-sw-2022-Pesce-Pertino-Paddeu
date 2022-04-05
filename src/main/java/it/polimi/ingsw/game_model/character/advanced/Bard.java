package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.school.DiningHall;
import it.polimi.ingsw.game_model.school.Entrance;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.List;

public class Bard extends AdvancedCharacter{

    public Bard(Game game){
        super(AdvancedCharacterType.BARD, game);
    }

    /**
     * You may exchange up to 2 students between your entrance and your dining room.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes) {
        if(!validateArgs(attributes)) {
            return false;
        }
        Player player = (Player) attributes[0];
        List<Integer> studentsFromEntrance = (List<Integer>) attributes[1];
        List<ColorCharacter> studentsFromDiningHall = (List<ColorCharacter>) attributes[2];


        Entrance playerEntrance = player.getSchool().getEntrance();
        DiningHall playerDiningHall = player.getSchool().getDiningHall();

        //TODO è necessario controllare che la dimensione dei due array sia uguale?

        // Adding the students to the Dining table (no need to store them they are saved with a counter)
        for(Integer i : studentsFromEntrance) {
            playerDiningHall.getTableOfColor(playerEntrance.getStudent(i).getColor()).addStudent();
        }
        for(Integer i : studentsFromEntrance) {
            // Removing the student only after they are added to their dining table. Otherwise, indexes problems may occur.
            playerEntrance.moveStudent(i);
        }
        // Adding the student to the Entrance
        for(ColorCharacter color : studentsFromDiningHall) {
            playerEntrance.addAllStudents(
                    playerDiningHall.getTableOfColor(color).removeStudent(1)
            );
        }
        return true;
    }

    @Override
    protected boolean validateArgs(Object... args) {
        if(args.length != 3) {
            return false;
        }

        try {
            Player player = (Player) args[0];
            List<Integer> studentsFromEntrance = (List<Integer>)args[1];
            List<ColorCharacter> studentsFromDiningHall = (List<ColorCharacter>)args[2];
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
