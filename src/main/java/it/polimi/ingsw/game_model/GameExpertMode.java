package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

/**
 * Class to add expert mode rules to the game model
 */
public class GameExpertMode extends Game {
    public static final int NUMBER_OF_ADVANCED_CARD = 3;
    private int treasury = 20;

    /**
     * @param playerNums the game number of players.
     */
    public GameExpertMode(int playerNums) {
        super(playerNums);
        isExpert = true;
    }

    /**
     * Gives at each player a coin.
     */
    private void setUpMoneyToPlayer(){
        for(Player player: players){
            treasury = player.addMoney(treasury);
        }
    }

    /**
     * Set up the game board by randomly adding the students to the islands and instantiating the cloud cards. In this case 3 advanced cards are also picked
     * and made playable.
     * @throws BagEmptyException if the bag is empty while drawing a student from it. (impossible to verify in this scenario since this method is called at the beginning of the match, when the bag is full)
     */
    @Override
    public void setupBoard() throws BagEmptyException {
        super.setupBoard();
        terrain.pickAdvancedCard(this);
        setUpMoneyToPlayer();
    }

    /**
     * In addition to move students to the dining hall, if the number of students on the dining hall becomes
     * multiple of 3, give the player a coin.
     * @param player current player moving student
     * @param color color of student
     * @throws TooManyStudentsException dining table of given color is full
     * @see Player current player
     */
    @Override
    public void moveStudentToDiningHall(Player player, ColorCharacter color) throws TooManyStudentsException {
        player.moveStudentToDiningHall(color);
        updateTeacherOwnership(player,color);
        if(player.getSchool().getDiningHall().getTableOfColor(color).getNumberOfStudents() % 3 == 0){
            treasury = player.addMoney(treasury);
        }
    }

    /**
     * Returns the current coins in the game treasury.
     * @return the current coins in the game treasury.
     */
    public int getTreasury() {
        return treasury;
    }

    /**
     * Adds the specified amount of coins to the treasury.
     * @param moneyToAdd the number of coins to add to the game treasury.
     */
    public void addMoneyToTreasury(int moneyToAdd) { treasury += moneyToAdd; }

    @Override
    public void runNotify(CommunicationMessage.MessageType type){
        notify(new MoveMessage(this, type, true));
    }
}
