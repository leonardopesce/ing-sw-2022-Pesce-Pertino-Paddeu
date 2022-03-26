package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;

public class Bartender extends AdvancedCharacter{
    public Bartender(Game game){
        super(AdvancedCharacterType.BARTENDER, game);
    }

    public void playEffect(){
        game.setTeacherOwnershipCalculator(
                new CalculatorTeacherOwnership(){
                    @Override
                    protected void addTeacherToPlayerWithHighestNumberOfStudentOfColor(Player currentPlayer, Player oldOwner, Teacher teacher){
                        Player newOwner = getNewPlayerOwnerOfTeacherOfColor(teacher);
                        if(currentPlayer.hasPlayedSpecialCard() &&
                                currentPlayer.getNumberOfStudentAtTableOfColor(teacher.getColor()) >=
                                newOwner.getNumberOfStudentAtTableOfColor(teacher.getColor())){
                                newOwner.getSchool().addTeacher(teacher);
                        }
                        else {
                            normalOwnershipCondition(oldOwner, teacher);
                        }
                    }
                }
        );
    }
}
