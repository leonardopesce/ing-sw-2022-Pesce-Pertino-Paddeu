package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.IslandNotPresentException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;
import javafx.util.Pair;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Game {
    private final String NO_NICKNAME = "";
    protected List<Player> players;
    protected int[] planningOrder, actionOrder; // [1,2,3,0], [3,1,0,2]
    protected BagOfStudents bag;
    protected Terrain terrain;
    protected MotherNature motherNature;


    public Game() {
        players = new ArrayList<Player>();
        terrain = new Terrain();
        bag = new BagOfStudents();

        /*
         * Placing mother nature in a random island between 0 and 11.
         * Each island is recognized by an id. Mother nature position is equal to
         * the id of the island she is currently on.
         */
        motherNature = new MotherNature(new Random().nextInt(12));
    }

    /**
     * Handle the game status.
     * Each game has a defined structure:
     * <ol>
     *     <li>
     *         Before the game starts:
     *         <ol>
     *             <li>Gameboard setup</li>
     *             <li>First turn planning phase setup<li/>
     *         </ol>
     *     </li>
     *     <li>
     *         Once the game is started and not finshed yet:
     *         <ol>
     *             <li>Planning Phase foreach player</li>
     *             <li>Action Phase foreach player</li>
     *         </ol>
     *     </li>
     * </ol>
     */
    public final void start(){
        createPlanningOrder();
        setupBoard();
        while(!winner().equals(NO_NICKNAME)){
            refillClouds();
            // every "turn" is divided in planning phase and action phase
            planningPhase();
            createActionPhaseOrder();
            for(int i = 0; i < players.size() && winner().equals(NO_NICKNAME); i++){
                actionPhaseStudents(players.get(i));
            }
            createNextPlanningOrder();
        }
    }

    public final void actionPhaseStudents(Player pl){
        pl.moveStudents(terrain.getIslands());
        updateProfessorsOwnership(pl);
        moveMotherNature(pl.getDiscardedCard().getPossibleSteps());
        evaluateInfluences();
        pl.resetPlayedSpecialCard();
    }

    public void setUpIslandTower(Island island, Player owner){
        //adds back tower to owner player
        if(!island.getTowers().isEmpty()){
            for(Player pl: players){
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
            Player mostInfluencing = players.stream().max((pl1, pl2) ->
                    Math.max(playerInfluence(pl1, island), playerInfluence(pl2, island))).get();

            setUpIslandTower(island, mostInfluencing);
        }
        catch(IslandNotPresentException e){
            e.printStackTrace();
        }
    }

    // influences given to player by towers
    public int playerTowerInfluence(Player pl, Island island){
        if(!island.getTowers().isEmpty() && island.getTowers().get(0).getColor() == pl.getColor()){
            return island.getTowers().size();
        }
        return 0;
    }

    // influences given to player by students on the island
    public int playerStudentInfluence(Player pl, Island island){
        int influence = 0;
        for(Teacher t: pl.getTeachers()){
            for(Student s: island.getStudents()){
                if(t.getColor() == s.getColor()){
                    influence++;
                }
            }
        }
        return influence;
    }

    public void updateProfessorsOwnership(Player pl1) {
        // for the dining table of pl1
        for (DiningTable table : pl1.getSchool().getDiningHall().getTables()) {
            // search in all players except pl1
            for (Player pl2 : players.stream().filter(player -> !player.getNickName().equals(pl1.getNickName())).collect(Collectors.toList())) {
                //Search in all the teacher of players pl2
                for (Teacher t : pl2.getTeachers()) {
                    updateProfessorOwnershipCondition(t, table, pl1, pl2);
                }
            }
        }
    }

    public final void normalUpdateProfessorOwnership(Teacher t, DiningTable table, Player pl1, Player pl2){
        if (t.getColor() == table.getColor() &&
                table.getNumberOfStudents() > pl2.getDiningTableWithColor(table.getColor()).getNumberOfStudents()) {
            pl1.getSchool().addTeacher(t);
            pl2.getTeachers().remove(t);
        }
    }


    /**
     * Given the max possible steps, it lets the user choose how many steps mother nature
     * has to do and then it moves her.
     *
     * @param maxSteps: max possible steps that mother nature can actually perform
     *                (based on the value of the assistant card played).
     */
    public void moveMotherNature(int maxSteps){
        //TODO with the number of step selected (event base) calculate the value of the new position
        //TODO keep in mind the possibility of a advanced card
        int selectedStep = new Random().nextInt(maxSteps);

        motherNature.moveOfIslands(terrain, selectedStep);
    }

    /**
     * Setup the game board by randomly adding the students to the islands and istantiating
     * the cloud cards.
     */
    private void setupBoard() {
        /*
        * Filling the bag with 10 students to setup the board. (2 students foreach color).
        * */
        bag.addStudentsFirstPhase();

        /*
        * Setting up the students foreach island (the one with mother nature and its opposite are not considered).
        * */
        for(Island island : terrain.getIslands()) {
            if(island.getId() != motherNature.getPosition() && island.getId() != ((motherNature.getPosition() + 6) % terrain.getIslands().size())) {
                island.addStudent(bag.drawStudentFromBag());
            }
        }

        /*
        * Filling the bag with the remaining 120 students.
        * */
        bag.addStudentsSecondPhase();

        /*
        * Creating cloud cards (one foreach player).
        * */
        for(int i=0; i<players.size(); i++) {
            this.terrain.addCloudCard(new CloudCard());
        }
    }

    public String winner(){
        //TODO
        //implements a function that returns the player which won -1 otherwise
        for(Player pl: players){
            if(pl.getTowersAvailable() == 0){
                return pl.getNickName();
            }
        }


        return NO_NICKNAME;
    }

    /**
     * Following the planning order, it makes the players play their assistant card.
     */
    private void planningPhase(){
        for(int pl: planningOrder){
            players.get(pl).playAssistant();
        }
    }

    /**
     * Creates the planning order of the first round by randomly choosing a player from the list.
     */
    private void createPlanningOrder(){
        planningOrder[0] = (int) Math.round(Math.random() * players.size());
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    /**
     * Creates the planning order of the next turn.
     * The player which played the assistant card with the lowest value will start the Planning Phase of the next turn.
     */
    private void createNextPlanningOrder(){
        planningOrder[0] = actionOrder[0];
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    private void createActionPhaseOrder(){
        List<Pair<Integer, Integer>> playerAndValue = new ArrayList<>();
        //cycle to create all position
        for(int i = 0; i < players.size(); i++){
            playerAndValue.add(new Pair<>(i, players.get(i).getDiscardedCard().getValue()));
        }

        playerAndValue.sort((a,b) -> Math.max(a.getValue(), b.getValue()));

        for(int i = 0; i < players.size(); i++){
            actionOrder[i] = playerAndValue.get(i).getKey();
        }
    }

    public abstract void updateProfessorOwnershipCondition(Teacher t, DiningTable table, Player pl1, Player pl2);
    public abstract int playerInfluence(Player pl, Island island);
    public abstract void refillClouds();


}
