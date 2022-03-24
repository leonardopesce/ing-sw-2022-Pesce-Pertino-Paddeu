package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacterInfluenceType;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

public class GameExpertMode extends Game {
    public static final int NUMBER_OF_ADVANCED_CARD = 3;
    AdvancedCharacter playerCard;
    private Integer treasury = 20;

    public GameExpertMode(Game game){
        super(game);
        treasury = ((GameExpertMode)game).treasury;
    }

    public GameExpertMode(int playerNums) {
        super(playerNums);
        setUpMoneyToPlayer();
    }

    private void setUpMoneyToPlayer(){
        for(Player player: players){
            player.addMoney(treasury);
        }
    }

    @Override
    protected void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1) {
        switch(playerCard.getAdvanceCharacterType()){
            case BARTENDER:
                if (pl1.hasPlayedSpecialCard() && table1.getColor() == table2.getColor() &&
                        table1.getNumberOfStudents() >= table2.getNumberOfStudents()) {
                    pl1.getSchool().addTeacher(getTeacherOfColorFromAllPlayers(table1.getColor()));
                }
                break;

            default:
                normalUpdateProfessorOwnership(table1, table2, pl1);
                break;
        }
    }

    @Override
    protected int playerInfluence(Player pl, Island island){
        if(playerCard.getCategory() == AdvancedCharacterInfluenceType.INFLUENCE_CATEGORY){
            return ((AdvancedCharacterInfluenceType)playerCard).getPlayerInfluence(pl, island);
        }
        return new CalculatorInfluence().evaluate(pl, island);
    }

    @Override
    protected void pickAdvancedCards(){
        terrain.pickAdvancedCard(bag);
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
