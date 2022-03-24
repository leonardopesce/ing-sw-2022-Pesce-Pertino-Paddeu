package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;
import javafx.util.Pair;

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
    private final int NUMBER_OF_STUDENTS_ON_CLOUD;
    private final int[] INITIAL_NUMBER_OF_TOWER;
    private final int INITIAL_NUMBER_OF_STUDENTS_TO_DRAW;
    private final int NUMBER_OF_CLOUDS;
    private final int MAX_PLAYERS;
    protected final List<Player> players;
    private final int[] planningOrder, actionOrder;
    protected BagOfStudents bag;
    protected Terrain terrain;
    private final MotherNature motherNature;
    private boolean isStartable;


    public Game(int playerNums) {
        players = new ArrayList<>();
        terrain = new Terrain();
        bag = new BagOfStudents();
        planningOrder = new int[playerNums];
        actionOrder = new int[playerNums];
        MAX_PLAYERS = playerNums;
        NUMBER_OF_CLOUDS = playerNums;
        /*
         * Placing mother nature on a random island between 0 and 11.
         * Each island is recognized by an id. Mother nature position is equal to
         * the id of the island she is currently on.
         */
        motherNature = new MotherNature(new Random().nextInt(12));
        isStartable = false;

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
            default -> {
                throw new IllegalStateException("Failed to create a game. Exit...");
            }
        }
    }

    /**
     * Handle the game status.
     * <p>
     *     Each game has a defined structure:
     *     <ol>
     *         Before the game starts:
     *         <ol>
     *             <li>Gameboard setup
     *             <li>createPlanning
     *         </ol>
     *     </ol>
     */
    // controller
    public final void start() throws NotEnoughPlayerException {
        if(this.isStartable) {
            createPlanningOrder();
            setupBoard();
        }
        else throw new NotEnoughPlayerException("The game is not ready yet. Some players are missing.");
    }

    /**
     * Adds a player to the list of players if the game is not full and if the player's nickname has not been chosen yet.
     *
     * @param player  the player to be added to the game's player list
     * @throws TooManyPlayerException  if the game is already full
     * @throws NicknameAlreadyChosenException  if the player nickname has already been chosen by another player already added to the game (note that letter cases will be ignored, so "paolo" equals "PaOLo")
     *
     * @see Player
     */
    //controller
    public void addPlayer(Player player) throws TooManyPlayerException, NicknameAlreadyChosenException {
        // Checking whether the game is full or not.
        if(players.size() < MAX_PLAYERS){
            // Checking if the player nickname has already been chosen. In that case an exception will be raised.
            for(Player pl : players) {
                if(player.getNickname().equalsIgnoreCase(pl.getNickname())) throw new NicknameAlreadyChosenException("The nickname " + pl.getNickname() +  " has already been chosen by another player.");
            }

            // Effectively adding the player to the list.
            players.add(player);
            isStartable = players.size() == MAX_PLAYERS;

        }
        else {
            throw new TooManyPlayerException("The game as already reached the limit of " + MAX_PLAYERS + " players");
        }
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
     * Given the player who as just moved is students moves the controls that the professors are in the right schools
     *
     * @param pl1 the player that has just moved his students
     *
     * @see Player
     */
    //controller
    public void updateProfessorsOwnership(Player pl1) {
        // for the dining table of pl1
        for (DiningTable table : pl1.getSchool().getDiningHall().getTables()) {
            // search in all players except pl1
            for (Player pl2 : players.stream().filter(player -> !player.getNickname().equals(pl1.getNickname())).toList()) {
                //Search in all the teacher of players pl2
                updateProfessorOwnershipCondition(table, pl2.getDiningTableWithColor(table.getColor()), pl1);
            }
        }
    }

    //controller
    protected final void normalUpdateProfessorOwnership(DiningTable table1, DiningTable table2, Player pl1){
        if (table1.getNumberOfStudents() > table2.getNumberOfStudents()) {
            pl1.getSchool().addTeacher(getTeacherOfColorFromAllPlayers(table1.getColor()));
        }
    }

    //controller
    protected Teacher getTeacherOfColorFromAllPlayers(ColorCharacter color){
        for(Player player: players){
            if(player.hasTeacherOfColor(color)){
                return player.getTeacherOfColor(color);
            }
        }
        // if no player has the teacher you are the first one to get it
        return new Teacher(color);
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
     * Set up the game board by randomly adding the students to the islands and instantiating
     * the cloud cards.
     */
    protected void setupBoard() {
        /* Filling the bag with 10 students to set up the board. (2 students foreach color). */
        bag.addStudentsFirstPhase();

        /*
         * Setting up the students foreach island (the one with mother nature and its opposite are not considered).
         * */
        for (Island island : terrain.getIslands()) {
            if (island.getId() != motherNature.getPosition() && island.getId() != ((motherNature.getPosition() + 6) % terrain.getIslands().size())) {
                try {
                    island.addStudent(bag.drawStudentFromBag());
                } catch (BagEmptyException e) {
                    e.printStackTrace();
                }
            }
        }

        /* Creating the school and adding students and tower */
        setUpPlayersBoard();

        /* Filling the bag with the remaining 120 students. */
        bag.addStudentsSecondPhase();

        /* Creating cloud cards (one foreach player). */
        createCloudCards();
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

    /**
     * Creates the planning order of the first round by randomly choosing a player from the list and going clock-wise.
     */
    //controller
    protected void createPlanningOrder(){
        planningOrder[0] = (int) Math.round(Math.random() * players.size());
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    /**
     * Creates the planning order of the next turn.
     * The player which played the assistant card with the lowest value will start the Planning Phase of the next turn.
     */
    //controller
    public void createNextPlanningOrder(){
        planningOrder[0] = actionOrder[0];
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    private void setUpPlayersBoard(){
        for (int i = 0; i < players.size(); i++) {
            players.get(i).initialSetup(
                    bag.drawNStudentFromBag(INITIAL_NUMBER_OF_STUDENTS_TO_DRAW),
                    INITIAL_NUMBER_OF_TOWER[players.size()],
                    ColorTower.values()[players.size() % (players.size() == 4 ? 2 : 3)]);
        }
    }

    //controller
    public void createActionPhaseOrder(){
        List<Pair<Integer, Integer>> playerAndValue = new ArrayList<>();
        //cycle to create all position
        for(int i = 0; i < players.size(); i++){
            playerAndValue.add(new Pair<>(i, players.get(i).getDiscardedCard().getValue()));
        }

        //TODO controllare come si comporta in caso due giocatori giochino la stessa carta,
        // (in tal caso chi prima arriva, meglio alloggia)
        playerAndValue.sort((a,b) -> Math.max(a.getValue(), b.getValue()));

        for(int i = 0; i < players.size(); i++){
            actionOrder[i] = playerAndValue.get(i).getKey();
        }
    }

    //controller
    public void resetPlayerNumberOfMOvedStudents(){
        for(Player pl: players){
            pl.resetNumberOfMovedStudents();
        }
    }

    //controller
    public void playerMoveStudentToIsland(Player player, int student, int island){
        terrain.addStudentToIsland(player.getSchool().getEntrance().moveStudent(student), island);
    }

    //controller
    public void playerMoveStudentToDiningHall(Player player, int student){
        player.moveStudentToDiningHall(player.getSchool().getEntrance().moveStudent(student).getColor());
    }

    public Player getPlayerNumber(int x){
        return players.get(x);
    }

    //controller
    public int[] getPlanningOrder(){
        return planningOrder;
    }

    //controller
    public int[] getActionOrder() {
        return actionOrder;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    //da rivedere gli advanced character
    protected void pickAdvancedCards(){}

    //da rivedere con logica a turni
    public int studentsLeftToMove(Player player) {
        return NUMBER_OF_STUDENTS_ON_CLOUD - player.getNumberOfMovedStudents();
    }
    //controller
    private void createCloudCards() {
        for(int i = 0; i < NUMBER_OF_CLOUDS; i++){
            terrain.addCloudCard(new CloudCard(NUMBER_OF_STUDENTS_ON_CLOUD));
        }
    }
    //controller
    protected void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1) {
        normalUpdateProfessorOwnership(table1, table2, pl1);
    }
    //controller
    public void refillClouds() {
        for(CloudCard cloudCard: terrain.getCloudCards()){
            cloudCard.refill(bag.drawNStudentFromBag(NUMBER_OF_STUDENTS_ON_CLOUD));
        }
    }

    protected int playerInfluence(Player pl, Island island) {
        return new CalculatorInfluence(pl, island).evaluate();
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
}
