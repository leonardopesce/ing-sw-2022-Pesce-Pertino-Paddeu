package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

public class Landlord extends AdvancedCharacter{
    public Landlord(Game game){
        super(AdvancedCharacterType.LANDLORD, game);
    }

    /**
     * Chose a color of student: during the calculation of influence this turn, that color adds no influence.
     * @param color color chosen
     */
    public void playEffect(ColorCharacter color){
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    private final ColorCharacter COLOR_TO_EXCLUDE = color;
                    @Override
                    public int evaluateForPlayer(Player player, Island island){
                        return playerTowerInfluence(player, island) + playerStudentInfluenceWithoutColor(player,island, COLOR_TO_EXCLUDE);
                    }

                    private int playerStudentInfluenceWithoutColor(Player pl, Island island, ColorCharacter color){
                        int influence = 0;
                        for(Teacher t: pl.getTeachers()){
                            for(Student s: island.getStudents()){
                                if(t.getColor() == s.getColor() && s.getColor() != color){
                                    influence++;
                                }
                            }
                        }
                        return influence;
                    }
                }
        );
    }
}
