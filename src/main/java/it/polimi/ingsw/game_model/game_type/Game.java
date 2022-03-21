package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.custom_exceptions.IslandNotPresentException;
import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.custom_exceptions.NotEnoughPlayerException;
import it.polimi.ingsw.custom_exceptions.TooManyPlayerException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.character.MotherNature;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;
import javafx.util.Pair;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    private final String NO_NICKNAME = "";
    private final int numberOfPlayers;
    protected List<Player> players;
    protected int[] planningOrder, actionOrder; // [1,2,3,0], [3,1,0,2]
    protected BagOfStudents bag;
    protected Terrain terrain;
    protected MotherNature motherNature;
    protected boolean isStartable;
    public static final int NUMBER_OF_ADVANCED_CARD = 3;


    public Game(int playerNums) {
        numberOfPlayers = playerNums;
        players = new ArrayList<>();
        terrain = new Terrain();
        bag = new BagOfStudents();

        /*
         * Placing mother nature on a random island between 0 and 11.
         * Each island is recognized by an id. Mother nature position is equal to
         * the id of the island she is currently on.
         */
        motherNature = new MotherNature(new Random().nextInt(12));
        isStartable = false;
    }




    /**
     * Handle the game status.
     * <p>
     *     Each game has a defined structure:
     *     <ol>
     *         <li>
     *             Before the game starts:
     *             <ol>
     *                 <li>Gameboard setup
     *                 <li>First turn planning phase setup
     *             </ol>
     *         </li>
     *         <li>
     *             Once the game is started and not finshed yet:
     *             <ol>
     *                 <li>Planning Phase foreach player
     *                 <li>Action Phase foreach player
     *             </ol>
     *         </li>
     *     </ol>
     */
    public final void start() throws NotEnoughPlayerException {
        // TODO valutare la possibilità di aggiungere un oggetto turno che gestisca il turno.
        if(this.isStartable) {
            createPlanningOrder();
            setupBoard();
            while (!winner().equals(NO_NICKNAME)) {
                refillClouds();
                // every "turn" is divided in planning phase and action phase
                planningPhase();
                createActionPhaseOrder();
                for (int i = 0; i < players.size() && winner().equals(NO_NICKNAME); i++) {
                    actionPhaseStudents(players.get(i));
                }
                createNextPlanningOrder();
            }
        } else throw new NotEnoughPlayerException("The game is not ready yet. Some players are missing.");
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
    public void addPlayer(Player player) throws TooManyPlayerException, NicknameAlreadyChosenException {
        // Checking whether the game is full or not.
        if(players.size() < numberOfPlayers){
            // Checking if the player nickname has already been chosen. In that case an exception will be raised.
            for(Player pl : players) {
                if(player.getNickname().equalsIgnoreCase(pl.getNickname())) throw new NicknameAlreadyChosenException("The nickname " + pl.getNickname() +  " has already been chosen by another player.");
            }

            // Effectively adding the player to the list.
            players.add(player);
            isStartable = players.size() == numberOfPlayers;
        }
        else {
            throw new TooManyPlayerException("The game as already reached the limit of " + numberOfPlayers + " players");
        }
    }

    /**
     * Handle the action phase of the specified player.
     *
     * <p>
     *     An action phase consists in making the player move his students from his entrance to his students' tables or to
     *     islands. At this point any professor ownership change will be handled, Mother Nature will move and finally the
     *     evaluation will be calculated with consequent possible construction of towers.
     *
     * @param pl  the player of which the action phase is handled
     *
     * @see Player
     */
    public final void actionPhaseStudents(Player pl){
        playerMoveStudents(pl);
        updateProfessorsOwnership(pl);
        moveMotherNature(pl.getDiscardedCard().getPossibleSteps());
        evaluateInfluences();
        pl.resetPlayedSpecialCard();
    }

    public void setUpIslandTower(Island island, Player owner){
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
            //TODO trasformare la funzione in base al colore del giocatore
            Player mostInfluencing = players.stream().max((pl1, pl2) ->
                    Math.max(playerInfluence(pl1, island), playerInfluence(pl2, island))).get();

            setUpIslandTower(island, mostInfluencing);
        }
        catch(IslandNotPresentException e){
            e.printStackTrace();
        }
    }


    /**
     * Given a player and an island of the board, returns the influence given by the control of any tower on that island.
     * <p>
     *     For example, if <code>pl</code> equals the white player, and the <code>island</code> has size equals to 1 with
     *     a White Tower built on it, then the method returns 1. <br>
     *     Otherwise, if the island is bigger (aggregation of 2 or
     *     more islands), it means that there are more than one tower to be counted (according to our implementation choices,
     *     exactly the size of the island).
     *
     * @param pl       the player whose influence brought by towers we want to calculate
     * @param island   the island on which we want to calculate the influence brought by towers
     * @return the additional influence brought by towers on a specific island for a specific player.
     *
     * @see Player
     * @see Island
     */
    public int playerTowerInfluence(Player pl, Island island){
        if(!island.getTowers().isEmpty() && island.getTowers().get(0).getColor() == pl.getColor()){
            return island.getTowers().size();
        }
        return 0;
    }

    /**
     * Given a player and an island of the board, calculate the influence brought by the students on the island itself.
     *
     * <p>
     *     A student of a specific color, counts as 1 during the influence calculation for the player who owns the
     *     corresponding professor. <br>
     *     This method returns the influence the specified player has by counting, according to the rule mentioned before,
     *     the sum of all the students whose corresponding professors are owned by that player.
     *
     * @param pl      the player whose influence brought by the students of the colors he controls we want to calculate
     * @param island  the island on which we want to calculate the influence brought by students
     * @return the total influence brought by the students whose professors are controlled by the specified player
     *
     * @see Player
     * @see Island
     * @see Teacher
     * @see Student
     */
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

    /**
     *
     * @param pl1
     */
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

    public final void normalUpdateProfessorOwnership(DiningTable table1, DiningTable table2, Player pl1){
        if (table1.getNumberOfStudents() > table2.getNumberOfStudents()) {
            pl1.getSchool().addTeacher(getTeacherOfColorFromAllPlayers(table1.getColor()));
        }
    }

    public Teacher getTeacherOfColorFromAllPlayers(ColorCharacter color){
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
        for (Island island : terrain.getIslands()) {
            if (island.getId() != motherNature.getPosition() && island.getId() != ((motherNature.getPosition() + 6) % terrain.getIslands().size())) {
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
        for (int i = 0; i < players.size(); i++) {
            createCloudCard();
        }

        // pick 3 advanced Character if it's a Expert mode game
        pickAdvancedCards();
    }

    /**
     * Handles the win conditions logic.
     *
     * <p>
     *     A
     */
    public String winner(){
        //TODO
        //implements a function that returns the player which won -1 otherwise
        for(Player pl: players){
            if(pl.getTowersAvailable() == 0){
                return pl.getNickname();
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
     * Creates the planning order of the first round by randomly choosing a player from the list and going clock-wise.
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

        //TODO controllare come si comporta in caso due giocatori giochino la stessa carta,
        // (in tal caso chi prima arriva, meglio alloggia)
        playerAndValue.sort((a,b) -> Math.max(a.getValue(), b.getValue()));

        for(int i = 0; i < players.size(); i++){
            actionOrder[i] = playerAndValue.get(i).getKey();
        }
    }

    public void pickAdvancedCards(){}

    public abstract void playerMoveStudents(Player player);
    public abstract void createCloudCard();
    public abstract void updateProfessorOwnershipCondition(DiningTable table1, DiningTable table2, Player pl1);
    public abstract int playerInfluence(Player pl, Island island);
    public abstract void refillClouds();
}
