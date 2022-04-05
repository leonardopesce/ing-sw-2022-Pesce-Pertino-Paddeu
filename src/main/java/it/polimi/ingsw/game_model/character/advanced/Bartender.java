package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class Bartender extends AdvancedCharacter{
    public Bartender(Game game){
        super(AdvancedCharacterType.BARTENDER, game);
    }

    /**
     * During this turn, you take the control of the other players' teachers even if you have the same
     * amount of students in your room as the player currently controlling them.
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)){
            return false;
        }

        game.setTeacherOwnershipCalculator(
                new CalculatorTeacherOwnership(){
                    @Override
                    protected void addTeacherToPlayerWithHighestNumberOfStudentOfColor(Player currentPlayer, Player oldOwner, Teacher teacher){
                        Player newOwner = getNewPlayerOwnerOfTeacherOfColor(teacher);
                        if(currentPlayer.getNumberOfStudentAtTableOfColor(teacher.getColor()) >=
                                newOwner.getNumberOfStudentAtTableOfColor(teacher.getColor())){
                            newOwner.getSchool().addTeacher(teacher);
                        }
                        else {
                            normalOwnershipCondition(oldOwner, teacher);
                        }
                    }
                }
        );
        return true;
    }

    @Override
    protected boolean validateArgs(Object... args) {
        return args.length == 0;
    }
}
