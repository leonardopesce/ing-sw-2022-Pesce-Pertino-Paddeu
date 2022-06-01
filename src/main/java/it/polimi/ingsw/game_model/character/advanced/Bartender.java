package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.utils.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;

/**
 *      <dt><b>Bartender</b> - Cost: <b>2</b></dt>
 *       <p>
 *       <dd>
 *           <b>EFFECT</b>: During this turn, you take the control of the other players' teachers even if you have the same amount of students in your room as the player currently controlling them.
 *       </dd>
 */
public class Bartender extends AdvancedCharacter{

    /**
     * @param game the game decorated by this card.
     */
    public Bartender(Game game){
        super(AdvancedCharacterType.BARTENDER, game);
    }

    /**
     * @param attributes  the arguments requested by the character in order to be successfully played. In this case that array must be empty.
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
                            currentPlayer.getSchool().addTeacher(teacher);
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
        return args.length == this.getType().getArgsLength();
    }
}
