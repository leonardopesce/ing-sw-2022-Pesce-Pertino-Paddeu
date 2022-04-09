package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class GameExpertMode extends Game {
    public static final int NUMBER_OF_ADVANCED_CARD = 3;
    private int treasury = 20;

    public GameExpertMode(int playerNums) {
        super(playerNums);
    }

    private void setUpMoneyToPlayer(){
        for(Player player: players){
            treasury = player.addMoney(treasury);
        }
    }

    @Override
    public void setupBoard() throws BagEmptyException, TooManyStudentsException{
        super.setupBoard();
        terrain.pickAdvancedCard(this);
        setUpMoneyToPlayer();
    }

    @Override
    public void moveStudentToDiningHall(Player player, ColorCharacter color) throws TooManyStudentsException{
        player.moveStudentToDiningHall(color);
        if(player.getSchool().getDiningHall().getTableOfColor(color).getNumberOfStudents() % 3 == 0){
            treasury = player.addMoney(treasury);
        }
    }

    public int getTreasury() {
        return treasury;
    }
}
