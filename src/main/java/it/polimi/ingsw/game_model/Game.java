package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.utils.CalculatorInfluence;
import it.polimi.ingsw.game_model.utils.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;
import it.polimi.ingsw.observer.Observable;

import java.security.SecureRandom;
import java.util.*;

/**
 * <p>
 * This object manage the creation and development of the game:
 * </p>
 * <p>
 *     Set up the game board and handles the win condition logic
 * </p>
 */
public class Game extends Observable<MoveMessage> {
    public final int NUMBER_OF_STUDENTS_ON_CLOUD;
    protected final int[] INITIAL_NUMBER_OF_TOWER;
    protected final int INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    public final int NUMBER_OF_CLOUDS;
    public final int MAX_PLAYERS;
    protected final List<Player> players = new ArrayList<>();
    protected final BagOfStudents bag = new BagOfStudents();
    protected final Terrain terrain = new Terrain();
    protected boolean isExpert;
    private Player currentlyPlaying;
    private final SecureRandom random = new SecureRandom();

    /*
     * Placing mother nature on a random island between 0 and 11.
     * Each island is recognized by an id. Mother nature position is equal to
     * the id of the island she is currently on.
     */
    protected final MotherNature motherNature = new MotherNature(random.nextInt(12));
    protected CalculatorInfluence influenceCalculator = new CalculatorInfluence();
    protected CalculatorTeacherOwnership teacherOwnershipCalculator = new CalculatorTeacherOwnership();
    protected GamePhase gamePhase;

    /**
     * Given the number of players in the current game, set up the number of
     * students on the cloud cards, the number of students to draw from the
     * bag at the beginning of the game and the number of towers for each player.
     * @param playerNums number of players in the current game
     * @see CloudCard
     * @see BagOfStudents
     */
    public Game(int playerNums) {
        MAX_PLAYERS = playerNums;
        NUMBER_OF_CLOUDS = playerNums;
        gamePhase = GamePhase.GAME_PENDING;
        isExpert = false;

        switch (playerNums) {
            case 2 -> {
                NUMBER_OF_STUDENTS_ON_CLOUD = 3;
                INITIAL_NUMBER_OF_TOWER = new int[]{8,8};
                INITIAL_NUMBER_OF_STUDENTS_TO_DRAW = 7;
            }
            case 3 -> {
                NUMBER_OF_STUDENTS_ON_CLOUD = 4;
                INITIAL_NUMBER_OF_TOWER = new int[]{6,6,6};
                INITIAL_NUMBER_OF_STUDENTS_TO_DRAW = 9;
            }
            case 4 -> {
                NUMBER_OF_STUDENTS_ON_CLOUD = 3;
                INITIAL_NUMBER_OF_TOWER = new int[]{8,8,0,0};
                INITIAL_NUMBER_OF_STUDENTS_TO_DRAW = 7;
            }
            default -> throw new IllegalStateException("Failed to create a game. Exit...");
        }
    }

    /**
     * Set up the game board by randomly adding the students to the islands and instantiating the cloud cards.
     *
     * @throws BagEmptyException if the bag is empty while drawing a student from it. (impossible to verify in this scenario since this method is called at the beginning of the match, when the bag is full)
     */
    public void setupBoard() throws BagEmptyException {
        /* Filling the bag with 10 students to set up the board. (2 students foreach color). */
        bag.addStudentsFirstPhase();

        /* Setting up the students foreach island (the one with mother nature and its opposite are not considered). */
        for (Island island : terrain.getIslands()) {
            if (island.getId() != motherNature.getPosition() && island.getId() != ((motherNature.getPosition() + 6) % terrain.getIslands().size())) {
                island.addStudent(bag.drawStudentFromBag());
            }
        }

        /* Filling the bag with the remaining 120 students. */
        bag.addStudentsSecondPhase();

        /* Creating the school and adding students and tower */
        setUpPlayersBoard();

        /* Creating cloud cards (one foreach player). */
        createCloudCards();
    }

    /**
     * For each player playing give the color and the number of towers, and the initial
     * students.
     * @throws BagEmptyException when there aren't enough students in the bag to fill the player entrance.
     * @see BagOfStudents
     * @see it.polimi.ingsw.game_model.school.School
     */
    private void setUpPlayersBoard() throws BagEmptyException{
        for (int i = 0; i < players.size(); i++) {
            players.get(i).initialSetup(
                    bag.drawNStudentFromBag(INITIAL_NUMBER_OF_STUDENTS_TO_DRAW),
                    INITIAL_NUMBER_OF_TOWER[i],
                    ColorTower.values()[i % (players.size() == 4 ? 2 : 3)]);
        }
    }

    /**
     * Create cloud cards according to the max number of players
     * @see CloudCard
     */
    protected void createCloudCards() {
        for(int i = 0; i < NUMBER_OF_CLOUDS; i++){
            terrain.addCloudCard(new CloudCard());
        }
    }

    /**
     * Given a color evaluate teacher ownership for a given player in the current game
     * @param player player to evaluate
     * @param color teacher color
     * @see it.polimi.ingsw.game_model.character.basic.Teacher
     * @see CalculatorTeacherOwnership
     */
    public void updateTeacherOwnership(Player player, ColorCharacter color){
        teacherOwnershipCalculator.evaluate(player, color, players);
    }

    /**
     * Set up the influence calculator
     * @param calculator Influence calculator object
     * @see CalculatorInfluence
     */
    public void setInfluenceCalculator(CalculatorInfluence calculator) {
        this.influenceCalculator = calculator;
    }

    /**
     * Set the teacher ownership calculator
      * @param newOwnershipCalculator the new teacher ownership calculator to set.
     * @see CalculatorTeacherOwnership
     */
    public void setTeacherOwnershipCalculator(CalculatorTeacherOwnership newOwnershipCalculator) { this.teacherOwnershipCalculator = newOwnershipCalculator; }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Given an island calculate influence on that island, whether there is single players
     * influence or teams influence
     * @param islandID island to calculate influence
     * @see Island
     * @see CalculatorInfluence
     */
    public void evaluateInfluences(int islandID) {
        Optional<Player> mostInfluencePlayer = influenceCalculator.evaluate(players, terrain.getIslandWithId(islandID));
        mostInfluencePlayer.ifPresent(player -> setUpIslandTower(terrain.getIslandWithId(islandID), player));
    }

    /**
     * Given an island and a player owner of a tower, set up that tower on the island
     * @param island where to place the tower
     * @param owner player owner of the tower
     * @see Island
     */
    private void setUpIslandTower(Island island, Player owner){
        //adds back tower to owner player
        for(Player pl: players){
            //remove tower from old owner
            if(!island.getTowers().isEmpty() && pl.getColor() == island.getTowers().get(0).getColor()){
                pl.addNTowers(island.getTowers().size());
                island.getTowers().clear();
            }
        }
        //remove towers from most influencing player
        island.getTowers().addAll(owner.removeNTowers(island.getSize()));

        //If needed, merges surrounding islands
        checkMergeIsland(island, terrain.getNextIsland(island));
        checkMergeIsland(island, terrain.getPreviousIsland(island));
    }

    /**
     * Given two islands merge them if there is a tower of the same color on each island
     * @param island1 first island to merge
     * @param island2 second island to merge
     * @see Terrain
     * @see Island
     */
    private void checkMergeIsland(Island island1, Island island2){
        if(!island2.getTowers().isEmpty() && island2.getTowers().get(0).getColor() == island1.getTowers().get(0).getColor()) {
            terrain.mergeIsland(island1, island2);
        }
    }

    /**
     * A method to handle game winning mechanics
     * <dl>
     *      <dt><b>First condition</b> - <b>Tower placement</b></dt>
     *      <dd>
     *       The game ends immediately when a player build their last tower. That player wins the game
     *      </dd>
     *      <dt><b>Second condition</b> - <b>Min groups of islands</b></dt>
     *          <dd>
     *             The game ends immediately when only three groups of islands remain on the table
     *          </dd>
     *      <dt><b>Third condition</b> - <b>End of the round</b></dt>
     *       <dd>
     *       The game ends at the end of the round where the last Student has
     *       been drawn from the bag or should any player ever run out of Assistant
     *       cards in their hand.
     *
     *       If there are not enough Students, players play
     *       their turns without taking new Students from a Cloud tile.
     *
     *       The player who has built the most Towers on Islands wins the game.
     *       In case of a tie, the player who controls the most Professors
     *       </dd>
     * </dl>
     *
     * @return an array of string representing the winner(s) nickname(s in case of 4 player match)
     */
    public String[] winner(){
        // First condition: a player has built all his towers in 4 players only the first two players have towers.
        for(int i = 0; i < players.size(); i++){
            if(INITIAL_NUMBER_OF_TOWER[i] != 0 && players.get(i).getTowersAvailable() == 0){
                return getPlayerOfColor(players.get(i).getColor()).stream().map(Player::getNickname).toArray(String[]::new);
            }
        }

        boolean draw = false;
        int[] numberOfTower = {0, 0, 0};
        if(terrain.getIslands().size() <= 3 ||
                (bag.isEmpty() && gamePhase == GamePhase.NEW_ROUND) ||
                (players.stream().allMatch(pl -> pl.getDeckAssistants().getSize() == 0) && gamePhase == GamePhase.NEW_ROUND)){

            for(Island is: terrain.getIslands()){
                if(!is.getTowers().isEmpty()) {
                    numberOfTower[is.getTowers().get(0).getColor().ordinal()] += is.getTowers().size();
                }
            }
            int max = 0;
            for(int i = 1; i < numberOfTower.length; i++){
                if(numberOfTower[max] < numberOfTower[i]){
                    max = i;
                    draw = false;
                }
                else if(numberOfTower[max] == numberOfTower[i]){
                    draw = true;
                }
            }

            if(draw){
                //if there are more player with the same number of tower
                draw = false;
                // inside max, it's stored the position of the first player with the maximum number of tower
                int maxProfessor = max;
                // get the player (color) with the maximum number of professor
                for(int i = max + 1; i < numberOfTower.length; i++){
                    if(numberOfTower[i] == numberOfTower[max]){
                        if(getPlayerOfColor(ColorTower.values()[i]).stream().mapToInt(pl -> pl.getTeachers().size()).sum()
                                > getPlayerOfColor(ColorTower.values()[maxProfessor]).stream().mapToInt(pl -> pl.getTeachers().size()).sum()){
                            maxProfessor = i;
                            draw = false;
                        }
                        else{
                            draw = true;
                        }
                    }
                }
                //If again it's a draw all the player with the same number of tower and Professors won
                if(draw){
                    List<Player> winnerPlayers = new ArrayList<>(getPlayerOfColor(ColorTower.values()[maxProfessor]));

                    for(int i = maxProfessor + 1; i < numberOfTower.length; i++){
                        if(getPlayerOfColor(ColorTower.values()[i]).stream().mapToInt(pl -> pl.getTeachers().size()).sum()
                                == getPlayerOfColor(ColorTower.values()[maxProfessor]).stream().mapToInt(pl -> pl.getTeachers().size()).sum()) {
                            winnerPlayers.addAll(getPlayerOfColor(ColorTower.values()[i]));
                        }
                    }
                    return winnerPlayers.stream().map(Player::getNickname).toArray(String[]::new);
                }
                else{
                    return getPlayerOfColor(ColorTower.values()[maxProfessor]).stream().map(Player::getNickname).toArray(String[]::new);
                }
            }
            else{
                return getPlayerOfColor(ColorTower.values()[max]).stream().map(Player::getNickname).toArray(String[]::new);
            }
        }

        // Second condition: there are only 3 island left
        return new String[0];
    }

    /**
     * Given a ColorTower, it returns the player (or the players in case of a 4 players match) who owns the towers of that color.
     *
     * @param color the color of the tower owned by the player(s) we want to fetch.
     * @return a list of players containing the player (or the players in case of a 4 players match) who owns the towers of the specified color.
     *
     * @see ColorTower
     * @see Player
     */
    private List<Player> getPlayerOfColor(ColorTower color){
        return players.stream().filter(pl -> pl.getColor() == color).toList();
    }

    /**
     * Returns the game's terrain.
     * @return the game's terrain.
     *
     * @see Terrain
     */
    public Terrain getTerrain(){
        return terrain;
    }

    /**
     * Returns the game's bag.
     * @return the game's bag.
     *
     * @see BagOfStudents
     */
    public BagOfStudents getBag() { return bag; }

    /**
     * Returns the list of players which are currently playing the game.
     *
     * @return a list of players containing all the players which are currently playing the game.
     *
     * @see Player
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Returns the number of players which are currently playing the game.
     * @return the number of players which are currently playing the game.
     */
    public int getNumberOfPlayers() { return players.size(); }


    /**
     * Calculate students left to move for the current player
     * @param player current player
     * @return students left to move
     * @see Player
     */
    public int studentsLeftToMove(Player player) {
        return NUMBER_OF_STUDENTS_ON_CLOUD - player.getNumberOfMovedStudents();
    }

    /**
     * Given a new game phase, it sets the game to that phase.
     * @param gamePhase the game phase which has to be set.
     *
     * @see GamePhase
     */
    public void setUpGamePhase(GamePhase gamePhase){
        this.gamePhase = gamePhase;
    }

    /**
     * <p>
     *  Move a student from the entrance or a cloud to the dining hall,
     *  start <code>updateTeacherOwnership</code>  to see if the change
     *  in the player's dining hall modify teacher ownerships
     * </p>
     *
     *
     * @param player current player moving student
     * @param color color of student
     * @throws TooManyStudentsException the dining hall of that color is full
     */
    public void moveStudentToDiningHall(Player player, ColorCharacter color) throws TooManyStudentsException{
        player.moveStudentToDiningHall(color);
        updateTeacherOwnership(player, color);
    }

    /**
     * Returns the current game phase.
     * @return the current game phase.
     *
     * @see GamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Check if the game is in expert mode
     * @return true if expert mode, otherwise false.
     */
    public boolean isExpert() {
        return isExpert;
    }

    /**
     * Returns the currently playing player.
     * @return the currently playing player.
     *
     * @see Player
     */
    public Player getCurrentlyPlayingPlayer() {
        return currentlyPlaying;
    }

    /**
     * Get the number of students which every player has to pick up at the beginning of the match in order to put them in his entrance.
     * @return the inisital number of students picked up by a player.
     */
    public int getINITIAL_NUMBER_OF_STUDENTS_TO_DRAW() {
        return INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    }

    /**
     * Sets the new currently playing player with the one with the given index in the players array.
     * @param indexCurrentlyPlayingPlayer the index of the new currently playing player which has to be set.
     */
    public void setCurrentlyPlaying(int indexCurrentlyPlayingPlayer) {
        this.currentlyPlaying = players.get(indexCurrentlyPlayingPlayer);
    }

    /**
     * Notify the RemoteGameView with the current state of the game.
     * @param type the message ID of the notified message.
     */
    public void runNotify(CommunicationMessage.MessageType type){
        notify(new MoveMessage(this, type));
    }

}