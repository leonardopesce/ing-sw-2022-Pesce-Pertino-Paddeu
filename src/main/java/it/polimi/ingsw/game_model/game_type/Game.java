package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.CalculatorProfessorOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

import java.util.*;

public class Game {
    //TODO scompattare questo macro oggetto game con:
    //  (1) la facciata effettiva del model
    //  (2) il game controller

    //TODO assegnare al gioco delle fasi: GAME_PENDING, GAME_STARTED, GAME_ENDED
    //TODO utilizzare una logica a turni per facilitare la gestione dei permessi.
    public static final String NO_NICKNAME = "";
    public static final int MOVE_TO_ISLAND = 0;
    public static final int MOVE_TO_DINING_HALL = 1;
    public final int NUMBER_OF_STUDENTS_ON_CLOUD;
    private final int[] INITIAL_NUMBER_OF_TOWER;
    private final int INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    public final int NUMBER_OF_CLOUDS;
    public final int MAX_PLAYERS;
    protected final List<Player> players;
    private final int[] planningOrder, actionOrder;
    protected final BagOfStudents bag;
    protected final Terrain terrain;
    private final MotherNature motherNature;
    private CalculatorInfluence influenceCalculator;
    private GamePhase gamePhase;

    public Game(int playerNums) {
        players = new ArrayList<>();
        terrain = new Terrain();
        bag = new BagOfStudents();
        planningOrder = new int[playerNums];
        actionOrder = new int[playerNums];
        MAX_PLAYERS = playerNums;
        NUMBER_OF_CLOUDS = playerNums;
        gamePhase = GamePhase.GAME_PENDING;
        /*
         * Placing mother nature on a random island between 0 and 11.
         * Each island is recognized by an id. Mother nature position is equal to
         * the id of the island she is currently on.
         */
        motherNature = new MotherNature(new Random().nextInt(12));

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
     * Set up the game board by randomly adding the students to the islands and instantiating
     * the cloud cards.
     */
    public void setupBoard() throws BagEmptyException, TooManyStudentsException{
        /* Filling the bag with 10 students to set up the board. (2 students foreach color). */
        bag.addStudentsFirstPhase();

        /*
         * Setting up the students foreach island (the one with mother nature and its opposite are not considered).
         * */
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

    private void setUpPlayersBoard() throws BagEmptyException, TooManyStudentsException{
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



    public CalculatorInfluence getInfluenceCalculator() {
        return influenceCalculator;
    }

    public void setInfluenceCalculator(CalculatorInfluence calculator) {
        this.influenceCalculator = calculator;
    }

    //controller
    protected void setUpIslandTower(Island island, Player owner){
        //adds back tower to owner player
        if(!island.getTowers().isEmpty()){
            for(Player pl: players){
                //remove tower from old owner
                if(pl.getColor() == island.getTowers().get(0).getColor()){
                    pl.addNTowers(island.getTowers().size());
                    island.getTowers().clear();
                }
            }
        }
        //removes towers from most influencer player
        owner.removeNTowers(island.getSize());
        for(int i = 0; i < island.getSize(); i++){
            island.getTowers().add(new Tower(owner.getColor()));
        }
    }

    public void evaluateInfluences(){
        try{
            Island island = terrain.getIslandWithId(motherNature.getPosition());
            // from the list of the player with the same color get the first player of the list with most influencing color
            Player mostInfluencing = getListsOfPlayersWithSameColor().stream().reduce(
                    (list1, list2) ->
                            list1.stream().mapToInt(pl -> playerInfluence(pl, island)).sum() >
                                    list2.stream().mapToInt(pl -> playerInfluence(pl, island)).sum() ? list1 : list2
            ).get().get(0);



            setUpIslandTower(island, mostInfluencing);
        }
        catch(IslandNotPresentException e){
            e.printStackTrace();
        }
    }



    /**
     * Given the max possible steps, it lets the user choose how many steps mother nature
     * has to do and then it moves her.
     *
     * @param selectedSteps: selected steps that mother nature has to perform
     *                (based on the value of the assistant card played).
     */
    //controller
    public void moveMotherNature(int selectedSteps){
        //TODO keep in mind the possibility of a advanced card
        motherNature.moveOfIslands(terrain, selectedSteps);
    }




    /**
     * Handles the win conditions logic.
     *
     * <p>
     *     A
     */
    //controller
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



    public void updateProfessorOwnerShip(Player player){
        new CalculatorProfessorOwnership(players, player);
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public Player getPlayerNumber(int x){
        return players.get(x);
    }

    public BagOfStudents getBag() {
        return bag;
    }

    public List<Player> getPlayers() {
        return players;
    }

    //controller
    public int[] getPlanningOrder(){
        return planningOrder;
    }

    //controller
    public int[] getActionOrder() { return actionOrder; }

    public int getNumberOfPlayers() { return players.size(); }

    //da rivedere gli advanced character
    protected void pickAdvancedCards(){}

    //da rivedere con logica a turni
    public int studentsLeftToMove(Player player) {
        return NUMBER_OF_STUDENTS_ON_CLOUD - player.getNumberOfMovedStudents();
    }

    protected int playerInfluence(Player pl, Island island) {
        return new CalculatorInfluence().evaluate(pl, island);
    }

    private List<List<Player>> getListsOfPlayersWithSameColor(){
        List<List<Player>> tmp = new ArrayList<>();
        for(Player player: players){
            boolean notAddedFlag = true;
            for(int i = 0; i < tmp.size(); i++){
                if(tmp.get(i).get(0).getColor() == player.getColor()){
                    tmp.get(i).add(player);
                    notAddedFlag = false;
                }
            }
            if(notAddedFlag){
                tmp.add(new ArrayList<>());
                tmp.get(tmp.size() - 1).add(player);
            }
        }
        return tmp;
    }

    public void setUpGamePhase(GamePhase gamePhase){
        this.gamePhase = gamePhase;
    }
}
