package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.*;
import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.CalculatorTeacherOwnership;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_model.world.CloudCard;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GameController {
    private final Game game;
    // private final GameView view;
    private int turn = 0;
    private final int[] planningOrder, actionOrder;

    public GameController(Game game) {
        this.game = game;
        // this.view = view;
        planningOrder = new int[game.MAX_PLAYERS];
        actionOrder = new int[game.MAX_PLAYERS];
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
                    // Impossible to reach. When the lobby is full the game starts.
                    e.printStackTrace();
                }
            }
            //TODO else mazzo già scelto
        } else {
            //TODO else nome già scelto
            throw new NicknameAlreadyChosenException("Already existing");
        }
    }

    /**
     * Adds a player to the list of players if the game is not full and if the player's nickname has not been chosen yet.
     *
     * @param player  the player to be added to the game's player list
     * @throws TooManyPlayerException  if the game is already full
     *
     * @see Player
     */
    protected void addPlayer(Player player) throws TooManyPlayerException {
        // Checking whether the game is full or not.
        if(game.getNumberOfPlayers() < game.MAX_PLAYERS){
            // Effectively adding the player to the list.
            game.getPlayers().add(player);
            if(game.getNumberOfPlayers() == game.MAX_PLAYERS){
                start();
            }
        }
        else {
            // Impossible to reach. When the game is full it automatically starts
            throw new TooManyPlayerException("The game as already reached the limit of " + game.MAX_PLAYERS + " players");
        }
    }

    /**
     * Handle the game initial status. Before the game starts:
     * <ol><li>create next Planning phase order <li> Gameboard setup <li> play a Turn </ol>
     */
    public final void start() {
        game.setUpGamePhase(GamePhase.GAME_PENDING);
        // at the beginning a random player is chosen
        createNextPlanningOrder(new Random().nextInt(game.getNumberOfPlayers()));
        try {
            game.setupBoard();
            playTurn();
        } catch (BagEmptyException | TooManyStudentsException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This function states the beginning of a new turn consisted in up to 4 planning phase and action phase
     */
    private void playTurn(){
        if (game.winner().equals(Game.NO_NICKNAME)) {
            game.setUpGamePhase(GamePhase.PLANNING_PHASE);
            refillClouds();
            // every "turn" is divided in planning phase and action phase
            nextPlanningPhase();
        }
    }

    /**
     * Plays the planning phase for the next player following the game planning order, in which every player plays an
     * assistant card. <p> If all player have already played an assistant card, moves to nextActionPhase() </p>
     */
    private void nextPlanningPhase(){
        if(turn < game.getNumberOfPlayers()){
            Player pl = game.getPlayerNumber(planningOrder[turn]);
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
     * It notifies the game model which card as been chosen and then tries to play the next planning phase.
     * @param player the player who request the action.
     * @param assistant the played assistant card.
     */
    public void selectAssistantCard(Player player, Assistant assistant){
        if(game.getPlayers().get(planningOrder[turn]).equals(player)){
            if(isAssistantCardPlayable(player, assistant)) {
                game.getPlayerNumber(planningOrder[turn]).playAssistant(assistant);

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

    /**
     *  Check if the assistant card is playable, the assistant card cannot be played if it was already played by another
     *  player, unless the player has no other card available to play.
     *
     * @param player the player who request the action
     * @param assistant the played assistant card
     * @return <strong>true</strong> if the assistant is playable, <strong>false</strong> otherwise.
     */
    private boolean isAssistantCardPlayable(Player player, Assistant assistant){
        List<Assistant> playedAssistant = new ArrayList<>();
        for(int i = 0; i < turn; i++){
            playedAssistant.add(game.getPlayers().get(planningOrder[i]).getDiscardedCard());
        }

        return !playedAssistant.contains(assistant) || playedAssistant.containsAll(player.getDeckAssistants().getAssistants());
    }

    /**
     * Creates the order for the action phase, based on the value of the assistant card played.
     */
    private void createActionPhaseOrder(){
        List<Pair<Integer, Integer>> playerAndValue = new ArrayList<>();
        //cycle to create all position
        for(int i = 0; i < game.getNumberOfPlayers(); i++){
            playerAndValue.add(new Pair<>(i, game.getPlayers().get(i).getDiscardedCard().getValue()));
        }

        playerAndValue.sort(Comparator.comparing(Pair::getValue));
        for(int i = 0; i < game.getNumberOfPlayers(); i++){
            actionOrder[i] = playerAndValue.get(i).getKey();
        }
    }

    /**
     * For every player reset his count of NumberOfMovedStudents
     */
    private void resetPlayerNumberOfMovedStudents(){
        for(Player pl: game.getPlayers()){
            pl.resetNumberOfMovedStudents();
        }
    }

    /**
     * Plays the action phase for the next player following the game action order, that starts with the movement of the
     * students. <p> If all player have already played, moves to a new turn with playTurn() </p>
     */
    private void nextActionPhase(){
        if(turn < game.getNumberOfPlayers() && game.winner().equals(Game.NO_NICKNAME)){
            game.setUpGamePhase(GamePhase.ACTION_PHASE_MOVING_STUDENTS);
            moveStudentPhase(game.getPlayerNumber(actionOrder[turn]));
        }
        else{
            turn = 0;
            createNextPlanningOrder(actionOrder[0]);

            playTurn();
        }
    }

    /**
     * Check if the player hasn't moved all his student
     * @param player the player who request the action
     */
    private void moveStudentPhase(Player player){
        //TODO creare observer per la view per mostrare la nuova fase{
        if(game.studentsLeftToMove(player) == 0){
            game.setUpGamePhase(GamePhase.ACTION_PHASE_MOVING_MOTHER_NATURE);
        }
    }

    // view callback for moving students
    public void playerMoveStudentToIsland(Player player, int student, int islandNumber){
        Player pl = game.getPlayerNumber(actionOrder[turn]);
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
        Player pl = game.getPlayerNumber(actionOrder[turn]);
        if(pl.equals(player)) {
            try {
                ColorCharacter diningHallColor = player.getSchool().getEntrance().moveStudent(student).getColor();
                game.moveStudentToDiningHall(player, diningHallColor);
                player.incrementNumberOfMovedStudents();
                game.updateTeacherOwnership(player, diningHallColor);
            } catch (TooManyStudentsException e) {
                //TODO non puoi più aggiungere studenti a quel tavolo visto che è pieno
                e.printStackTrace();
            }
            moveStudentPhase(player);
        }
        else{
            //TODO non è il tuo turno
        }
    }

    /**
     * Given the max possible steps, it lets the user choose how many steps mother nature
     * has to do, and then it moves her.
     *
     * @param player: the player who called the action
     * @param numberOfSteps: selected steps that mother nature has to perform
     *                (based on the value of the assistant card played and possible usage of advanced card).
     */
    public void moveMotherNatureOfSteps(Player player, int numberOfSteps){
        if(player.equals(game.getPlayerNumber(actionOrder[turn]))){
            if(1 <= numberOfSteps && numberOfSteps <= player.getDiscardedCard().getPossibleSteps()){
                game.getMotherNature().moveOfIslands(game.getTerrain(), numberOfSteps);

                game.evaluateInfluences(game.getMotherNature().getPosition());

                if(game.winner().equals(Game.NO_NICKNAME)) {
                    game.setUpGamePhase(GamePhase.ACTION_PHASE_CHOOSING_CLOUD);
                }
                else {
                    game.setUpGamePhase(GamePhase.GAME_ENDED);
                    endGame();
                }
            }
            else{
                //TODO non sei nel range di step valido
            }
        } else {
            //TODO non è il tuo turno
        }
    }

    private void endGame(){
        //TODO the game is ended
    }

    public void choseCloud(Player player, CloudCard cloudCard){
        if(player.equals(game.getPlayerNumber(actionOrder[turn]))){
            player.getSchool().getEntrance().addAllStudents(cloudCard.removeStudentsOnCloud());
            turn++;
            //TODO reset
            player.resetPlayedSpecialCard();
            game.setInfluenceCalculator(new CalculatorInfluence());
            game.setTeacherOwnershipCalculator(new CalculatorTeacherOwnership());

            nextActionPhase();
        } else {
            //TODO non è il tuo turno
        }
    }

    public void playAdvancedCard(Player player, AdvancedCharacter card, Object... args){
        if(player.equals(game.getPlayerNumber(actionOrder[turn])) && game.getGamePhase().toString().startsWith("ACTION_PHASE")){
            if(!player.hasPlayedSpecialCard() && player.getMoney() >= card.getType().getCardCost()){
                if(AdvancedCardController.checkArgument(card, args)){
                    player.setPlayedSpecialCard();
                    card.getType().incrementCardCost();
                    AdvancedCardController.playEffectOfCard(card, args);
                }
            }
        }
        else {
            //TODO fa il bravo non è il tuo turno
        }
    }


    private void refillClouds() {
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
    private void createNextPlanningOrder(int firstPlayerPlanningOrder){
        planningOrder[0] = firstPlayerPlanningOrder;
        for(int i = 1; i < game.getNumberOfPlayers(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % game.getNumberOfPlayers();
        }
    }

    public Player getCurrentPlayer() {
        if(game.getGamePhase().toString().startsWith("ACTION")) return game.getPlayers().get(actionOrder[turn]);
        else return game.getPlayers().get(planningOrder[turn]);
    }
}
