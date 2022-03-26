package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_view.GameView;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameController {
    private final Game game;
    private final GameView view;
    private int turn = 0;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    /**
    * @throws NicknameAlreadyChosenException  if the player nickname has already been chosen by another player
     * already added to the game (note that letter cases will be ignored, so "paolo" equals "PaOLo")
    */
    public void createPlayer(String name, DeckType type) throws NicknameAlreadyChosenException{
        if(!game.getPlayers().stream().map(pl -> pl.getNickname()).anyMatch(nm -> nm.equals(name))){
            if(!game.getPlayers().stream().map(pl -> pl.getDeckAssistants().getType()).anyMatch(val -> val.equals(type))) {
                try {
                    addPlayer(new Player(name, type));
                } catch (TooManyPlayerException e) {
                    e.printStackTrace();
                }
            }
            //TODO else mazzo già scelto
        }
        //TODO else nome già scelto
    }

    /**
     * Adds a player to the list of players if the game is not full and if the player's nickname has not been chosen yet.
     *
     * @param player  the player to be added to the game's player list
     * @throws TooManyPlayerException  if the game is already full
     *
     * @see Player
     */
    public void addPlayer(Player player) throws TooManyPlayerException {
        // Checking whether the game is full or not.
        if(game.getNumberOfPlayers() < game.MAX_PLAYERS){
            // Effectively adding the player to the list.
            game.getPlayers().add(player);
            if(game.getNumberOfPlayers() == game.MAX_PLAYERS){
                start();
            }
        }
        else {
            throw new TooManyPlayerException("The game as already reached the limit of " + game.MAX_PLAYERS + " players");
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
    public final void start() {
        game.setUpGamePhase(GamePhase.GAME_PENDING);
        // at the beginning a random player is chosen
        createNextPlanningOrder((int) Math.round(Math.random() * game.getNumberOfPlayers()));
        try {
            game.setupBoard();
            playTurn();
        } catch (BagEmptyException | TooManyStudentsException e) {
            e.printStackTrace();
        }
    }

    private void playTurn(){
        if (game.winner().equals(Game.NO_NICKNAME)) {
            game.setUpGamePhase(GamePhase.PLANNING_PHASE);
            refillClouds();
            // every "turn" is divided in planning phase and action phase
            nextPlanningPhase();
        }
    }

    private void nextPlanningPhase(){
        if(turn < game.getNumberOfPlayers()){
            Player pl = game.getPlayerNumber(game.getPlanningOrder()[turn]);
            //TODO vedere come implementare model observer per view
        }
        else{
            turn = 0;
            createActionPhaseOrder();
            resetPlayerNumberOfMovedStudents();
            nextActionPhase();
        }
    }

    /**
     * This function will be called when a player as chosen which Assistant card to play.
     * It notifies the game model which card as been chosen and then tries to play the next planning phase
     * @param
     */
    public void selectAssistantCard(Player player, Assistant assistant){
        if(game.getPlayers().get(game.getPlanningOrder()[turn]).equals(player)){
            if(isAssistantCardPlayable(player, assistant)) {
                game.getPlayerNumber(game.getPlanningOrder()[turn]).playAssistant(assistant);

                turn++;
                nextPlanningPhase();
            }
            else {
                //TODO non puoi giocare questa carta, è già stata giocata
            }
        }
        else{
            //TODO non è il tuo turno stai fermo
        }
    }

    private boolean isAssistantCardPlayable(Player player, Assistant assistant){
        List<Assistant> playedAssistant = new ArrayList<>();
        for(int i = 0; i < turn; i++){
            playedAssistant.add(game.getPlayers().get(game.getPlanningOrder()[i]).getDiscardedCard());
        }

        return !playedAssistant.contains(assistant) || playedAssistant.containsAll(player.getDeckAssistants().getAssistants());
    }

    public void createActionPhaseOrder(){
        List<Pair<Integer, Integer>> playerAndValue = new ArrayList<>();
        //cycle to create all position
        for(int i = 0; i < game.getNumberOfPlayers(); i++){
            playerAndValue.add(new Pair<>(i, game.getPlayers().get(i).getDiscardedCard().getValue()));
        }

        playerAndValue.sort(Comparator.comparing(Pair::getValue));

        for(int i = 0; i < game.getNumberOfPlayers(); i++){
            game.getActionOrder()[i] = playerAndValue.get(i).getKey();
        }
    }

    public void resetPlayerNumberOfMovedStudents(){
        for(Player pl: game.getPlayers()){
            pl.resetNumberOfMovedStudents();
        }
    }

    private void nextActionPhase(){
        if(turn < game.getNumberOfPlayers() && game.winner().equals(Game.NO_NICKNAME)){
            game.setUpGamePhase(GamePhase.ACTION_PHASE_MOVING_STUDENTS);
            moveStudentPhase(game.getPlayerNumber(game.getActionOrder()[turn]));
        }
        else{
            turn = 0;
            createNextPlanningOrder(game.getActionOrder()[0]);
            playTurn();
        }
    }

    private void moveStudentPhase(Player player){
        if(game.studentsLeftToMove(player) > 0){
            //TODO creare observer per la view per mostrare la nuova fase

        }
        else {
            game.updateProfessorsOwnership(player);
            view.showNewProfessorsOwnership();
            moveMotherNature(player);
        }
    }

    // view callback for moving students
    public void playerMoveStudentToIsland(Player player, int student, int islandNumber){
        Player pl = game.getPlayerNumber(game.getActionOrder()[turn]);
        if(pl.equals(player)) {
            //TODO display movement of student directly in view
            game.getTerrain().addStudentToIsland(player.getSchool().getEntrance().moveStudent(student), islandNumber);
            player.incrementNumberOfMovedStudents();

            moveStudentPhase(pl);
        }
        else{
            //TODO non è il tuo turno
        }
    }

    public void playerMoveStudentToDiningHall(Player player, int student){
        Player pl = game.getPlayerNumber(game.getActionOrder()[turn]);
        if(pl.equals(player)) {
            try {
                player.moveStudentToDiningHall(player.getSchool().getEntrance().moveStudent(student).getColor());
            } catch (TooManyStudentsException e) {
                //TODO non puoi più aggiungere studenti a quel tavolo visto che è pieno
                e.printStackTrace();
            }
            player.incrementNumberOfMovedStudents();
            moveStudentPhase(pl);
        }
            else{
            //TODO non è il tuo turno
        }
    }

    private void moveMotherNature(Player player){
        view.showMotherNaturePossibleStep(player.getDiscardedCard().getPossibleSteps());
    }

    public void moveMotherNatureOfSteps(int x){
        game.moveMotherNature(x);
        game.evaluateInfluences();
        view.showNewInfluence();
        nextActionPhase();
    }

    public void refillClouds() {
        for(CloudCard cloudCard: game.getTerrain().getCloudCards()){
            try {
                cloudCard.refill(game.getBag().drawNStudentFromBag(game.NUMBER_OF_STUDENTS_ON_CLOUD));
            } catch (BagEmptyException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the planning order of the next turn.
     * The player which played the assistant card with the lowest value will start the Planning Phase of the next turn.
     */
    public void createNextPlanningOrder(int firstPlayerPlanningOrder){
        game.getPlanningOrder()[0] = firstPlayerPlanningOrder;
        for(int i = 1; i < game.getNumberOfPlayers(); i++){
            game.getPlanningOrder()[i] = (game.getPlanningOrder()[i-1] + 1) % game.getNumberOfPlayers();
        }
    }
}
