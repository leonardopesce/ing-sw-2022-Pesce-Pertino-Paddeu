package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.school.DiningTable;

public class GameExpertMode extends Game {
    public static final int NUMBER_OF_ADVANCED_CARD = 3;
    AdvancedCharacter playerCard;
    private Integer treasury = 20;

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
    protected void pickAdvancedCards(){
        terrain.pickAdvancedCard(bag);
    }

    /*private int playerStudentInfluenceWithoutColor(Player pl, Island island, ColorCharacter color){
        int influence = 0;
        for(Teacher t: pl.getTeachers()){
            for(Student s: island.getStudents()){
                if(t.getColor() == s.getColor() && s.getColor() != color){
                    influence++;
                }
            }
        }
        return influence;
    }*/

}
