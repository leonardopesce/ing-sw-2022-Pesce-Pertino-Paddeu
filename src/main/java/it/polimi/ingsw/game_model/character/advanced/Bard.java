package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.school.DiningHall;
import it.polimi.ingsw.game_model.school.DiningTable;
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

        String playerNickname = (String) attributes[0];
        Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);
        List<Integer> studentsFromEntrance = (List<Integer>) attributes[1];
        List<ColorCharacter> studentsFromDiningHall = (List<ColorCharacter>) attributes[2];

        Entrance playerEntrance = player.getSchool().getEntrance();
        DiningHall playerDiningHall = player.getSchool().getDiningHall();

        //TODO è necessario controllare che la dimensione dei due array sia uguale?
        if(studentsFromEntrance.size() == studentsFromDiningHall.size()){
            for(int i = 0; i < studentsFromEntrance.size(); i++) {
                try {
                    game.moveStudentToDiningHall(player, playerEntrance.moveStudent(studentsFromEntrance.get(i)).getColor());
                } catch (TooManyStudentsException e) {
                    return false;
                }
                playerEntrance.getStudents().add(
                        studentsFromEntrance.get(i),
                        playerDiningHall.getTableOfColor(studentsFromDiningHall.get(i)).removeStudent(1).get(0));
            }
        }
        return true;
    }

    @Override
    protected boolean validateArgs(Object... args) {
        if(args.length != 3) {
            return false;
        }

        try {
            String playerNickname = (String) args[0];
            Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);
            List<Integer> studentsFromEntrance = (List<Integer>)args[1];
            List<ColorCharacter> studentsFromDiningHall = (List<ColorCharacter>)args[2];
            ColorCharacter color = player.getSchool().getEntrance().getStudent(studentsFromEntrance.get(0)).getColor();
        } catch(Exception ex) {
            return false;
        }

        return true;
    }
}
