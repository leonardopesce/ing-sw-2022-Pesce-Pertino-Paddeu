package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class GameExpertMode extends Game {
    public static final int NUMBER_OF_ADVANCED_CARD = 3;
    private int treasury = 20;

    public GameExpertMode(int playerNums) {
        super(playerNums);
        isExpert = true;
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
    public void moveStudentToDiningHall(Player player, ColorCharacter color){
        player.moveStudentToDiningHall(color);
        updateTeacherOwnership(player,color);
        if(player.getSchool().getDiningHall().getTableOfColor(color).getNumberOfStudents() % 3 == 0){
            treasury = player.addMoney(treasury);
        }
    }

    public int getTreasury() {
        return treasury;
    }

    public void addMoneyToTreasury(int moneyToAdd) { treasury += moneyToAdd; }

    @Override
    public void runNotify(CommunicationMessage.MessageType type){
        notify(new MoveMessage(this, type, true));
    }
}
