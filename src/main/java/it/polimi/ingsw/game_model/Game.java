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

import java.util.*;

public class Game extends Observable<MoveMessage> {
    public final int NUMBER_OF_STUDENTS_ON_CLOUD;
    protected final int[] INITIAL_NUMBER_OF_TOWER;
    protected final int INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    public final int NUMBER_OF_CLOUDS;
    public final int MAX_PLAYERS;
    protected final List<Player> players = new ArrayList<>();
    protected final BagOfStudents bag = new BagOfStudents();
    protected final Terrain terrain = new Terrain();
    private Player currentlyPlaying;
    private final Random random = new Random();

    /*
     * Placing mother nature on a random island between 0 and 11.
     * Each island is recognized by an id. Mother nature position is equal to
     * the id of the island she is currently on.
     */
    protected final MotherNature motherNature = new MotherNature(random.nextInt(12));
    protected CalculatorInfluence influenceCalculator = new CalculatorInfluence();
    protected CalculatorTeacherOwnership teacherOwnershipCalculator = new CalculatorTeacherOwnership();
    protected GamePhase gamePhase;

    public Game(int playerNums) {
        MAX_PLAYERS = playerNums;
        NUMBER_OF_CLOUDS = playerNums;
        gamePhase = GamePhase.GAME_PENDING;

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
     */
    public void setupBoard() throws BagEmptyException, TooManyStudentsException{
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

    private void setUpPlayersBoard() throws BagEmptyException{
        for (int i = 0; i < players.size(); i++) {
            players.get(i).initialSetup(
                    bag.drawNStudentFromBag(INITIAL_NUMBER_OF_STUDENTS_TO_DRAW),
                    INITIAL_NUMBER_OF_TOWER[i],
                    ColorTower.values()[i % (players.size() == 4 ? 2 : 3)]);
        }
    }

    protected void createCloudCards() {
        for(int i = 0; i < NUMBER_OF_CLOUDS; i++){
            terrain.addCloudCard(new CloudCard());
        }
    }

    public void updateTeacherOwnership(Player player, ColorCharacter color){
        teacherOwnershipCalculator.evaluate(player, color, players);
    }

    public void setInfluenceCalculator(CalculatorInfluence calculator) {
        this.influenceCalculator = calculator;
    }

    public void setTeacherOwnershipCalculator(CalculatorTeacherOwnership newOwnershipCalculator) { this.teacherOwnershipCalculator = newOwnershipCalculator; }

    public MotherNature getMotherNature() {
        return motherNature;
    }

    public void evaluateInfluences(int islandID) {
        Optional<Player> mostInfluencePlayer = influenceCalculator.evaluate(players, terrain.getIslandWithId(islandID));
        mostInfluencePlayer.ifPresent(player -> setUpIslandTower(terrain.getIslandWithId(islandID), player));
    }

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

    private void checkMergeIsland(Island island1, Island island2){
        if(!island2.getTowers().isEmpty() && island2.getTowers().get(0).getColor() == island1.getTowers().get(0).getColor()) {
            terrain.mergeIsland(island1, island2);
        }
    }

    /**
     * Handles the win conditions logic.
     */
    //TODO condition when all players finish their assistant card
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
                players.stream().allMatch(pl -> pl.getDeckAssistants().getSize() == 0)){

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

    private List<Player> getPlayerOfColor(ColorTower color){
        return players.stream().filter(pl -> pl.getColor() == color).toList();
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public BagOfStudents getBag() { return bag; }

    public List<Player> getPlayers() { return players; }

    public int getNumberOfPlayers() { return players.size(); }

    public int studentsLeftToMove(Player player) {
        return NUMBER_OF_STUDENTS_ON_CLOUD - player.getNumberOfMovedStudents();
    }

    public void setUpGamePhase(GamePhase gamePhase){
        this.gamePhase = gamePhase;
    }

    public void moveStudentToDiningHall(Player player, ColorCharacter color) throws TooManyStudentsException{
        player.moveStudentToDiningHall(color);
        updateTeacherOwnership(player, color);
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }


    public Player getCurrentlyPlayingPlayer() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(int indexCurrentlyPlayingPlayer) {
        this.currentlyPlaying = players.get(indexCurrentlyPlayingPlayer);
    }

    public void runNotify(CommunicationMessage.MessageType type){
        notify(new MoveMessage(this, type));
    }

}