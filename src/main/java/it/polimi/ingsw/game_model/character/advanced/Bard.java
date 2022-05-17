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

        if(studentsFromEntrance.size() == studentsFromDiningHall.size()){
            for(int i = 0; i < studentsFromEntrance.size(); i++) {
                try {
                    game.moveStudentToDiningHall(player, playerEntrance.moveStudent(studentsFromEntrance.get(i)).getColor());
                } catch (TooManyStudentsException e) {
                    // Impossible to reach since we make a control in the validateArgs function
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
        if(args.length != this.getType().getArgsLength()) {
            return false;
        }

        try {
            // Verifying if the cast are possible, if not the args passed are wrong so the card is not playable.
            String playerNickname = (String) args[0];
            Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);
            List<Integer> studentsFromEntrance = (List<Integer>)args[1];
            List<ColorCharacter> studentsFromDiningHall = (List<ColorCharacter>)args[2];

            // Verifying if the 2 arrays passed are of the same dimension, otherwise the card is not playable.
            // Verifying that there are not 2 equals indexes in the studentFromEntrance list. Otherwise, the args are wrong and the card cannot be played.
            // Verifying that all the indexes in the studentsFromEntrance array are not out of bound.
            if(studentsFromEntrance.size() != studentsFromDiningHall.size() ||
                    playerNickname == null ||
                    !game.getPlayers().stream().map(Player::getNickname).toList().contains(playerNickname) ||
                    studentsFromEntrance.contains(null) ||
                    studentsFromDiningHall.contains(null) ||
                    studentsFromEntrance.stream().distinct().toList().size() != studentsFromEntrance.size() ||
                    studentsFromEntrance.stream().filter(index -> index >= 0 && index < game.getINITIAL_NUMBER_OF_STUDENTS_TO_DRAW()).toList().size() != studentsFromEntrance.size()
            ) {
                return false;
            } else {
                // Verifying that there are enough slots in the tables the player want to place the students.
                // If the table is full, the card is not playable.
                int[] tablesDimension = new int[5];
                for(int i=0;i<5;i++) {
                    int finalI = i;
                    tablesDimension[i] = player.getSchool().getDiningHall().getTables()[i].getNumberOfStudents() - studentsFromDiningHall.stream().filter(color -> color.equals(ColorCharacter.values()[finalI])).toList().size();
                }
                for (Integer integer : studentsFromEntrance) {
                    tablesDimension[player.getSchool().getEntrance().getStudent(integer).getColor().ordinal()] += 1;
                }
                for(int i=0;i<5;i++) {
                    if(tablesDimension[i] > DiningTable.MAX_POSITIONS) return false;
                }
            }
        } catch(Exception ex) {
            return false;
        }

        return true;
    }
}
