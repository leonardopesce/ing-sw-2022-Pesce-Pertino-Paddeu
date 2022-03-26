package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

import java.util.*;

public class Game {
    public static final String NO_NICKNAME = "";
    public final int NUMBER_OF_STUDENTS_ON_CLOUD;
    private final int[] INITIAL_NUMBER_OF_TOWER;
    private final int INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    public final int NUMBER_OF_CLOUDS;
    public final int MAX_PLAYERS;
    protected final List<Player> players = new ArrayList<>();
    protected final BagOfStudents bag = new BagOfStudents();
    protected final Terrain terrain = new Terrain();
    /*
     * Placing mother nature on a random island between 0 and 11.
     * Each island is recognized by an id. Mother nature position is equal to
     * the id of the island she is currently on.
     */
    private final MotherNature motherNature = new MotherNature(new Random().nextInt(12));
    private CalculatorInfluence influenceCalculator = new CalculatorInfluence();
    private CalculatorTeacherOwnership teacherOwnershipCalculator = new CalculatorTeacherOwnership();
    private GamePhase gamePhase;

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

        /* Creating the school and adding students and tower */
        setUpPlayersBoard();

        /* Filling the bag with the remaining 120 students. */
        bag.addStudentsSecondPhase();

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

    private void createCloudCards() {
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

    public void evaluateInfluences() {
        Optional<Player> mostInfluencePlayer = influenceCalculator.evaluate(players, terrain.getIslandWithId(motherNature.getPosition()));
        mostInfluencePlayer.ifPresent(player -> setUpIslandTower(terrain.getIslandWithId(motherNature.getPosition()), player));
    }

    protected void setUpIslandTower(Island island, Player owner){
        //adds back tower to owner player
        for(Player pl: players){
            //remove tower from old owner
            if(!island.getTowers().isEmpty() && pl.getColor() == island.getTowers().get(0).getColor()){
                pl.addNTowers(island.getTowers().size());
                island.getTowers().clear();
            }
        }
        //remove towers from most influencing player
        owner.removeNTowers(island.getSize());
        for(int i = 0; i < island.getSize(); i++){
            island.getTowers().add(new Tower(owner.getColor()));
        }

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
    public String winner(){
        // First condition: a player has built all his towers.
        for(Player pl: players){
            if(pl.getTowersAvailable() == 0){
                return pl.getNickname();
            }
        }

        // Second condition: there are only 3 island left
        return NO_NICKNAME;
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public Player getPlayerNumber(int x){
        return players.get(x);
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
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}