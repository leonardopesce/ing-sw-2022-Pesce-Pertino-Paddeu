package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.custom_exceptions.NotEnoughPlayerException;
import it.polimi.ingsw.custom_exceptions.TooManyPlayerException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_view.GameView;

public class GameController {
    Game game;
    GameView view;
    int turn = 0;

    public GameController(Game game, GameView view) {
        this.game = game;
        this.view = view;
    }

    public void addPlayer(Player player){
        try {
            game.addPlayer(player);
        } catch (TooManyPlayerException | NicknameAlreadyChosenException e) {
            e.printStackTrace();
        }
    }

    public void startGame(){
        try {
            game.start();
        } catch (NotEnoughPlayerException e) {
            e.printStackTrace();
        }
        playTurn();
    }

    private void playTurn(){
        if (game.winner().equals(Game.NO_NICKNAME)) {
            game.refillClouds();
            // every "turn" is divided in planning phase and action phase
            nextPlanningPhase();
        }
    }

    private void nextPlanningPhase(){
        if(turn < game.getNumberOfPlayers()){
            Player pl = game.getPlayerNumber(game.getPlanningOrder()[turn]);
            view.showPlayerDeck(pl, pl.getDeckAssistants());
            turn++;
        }
        else{
            turn = 0;
            game.createActionPhaseOrder();
            game.resetPlayerNumberOfMOvedStudents();
            nextActionPhase();
        }
    }

    /**
     * This function will be called when a player as chosen which Assistant card to play.
     * It notifies the game model which card as been chosen and then tries to play the next planning phase
     * @param x
     */
    public void selectedAssistantCard(int x){
        game.getPlayerNumber(game.getPlanningOrder()[turn]).playAssistant(x);
        nextPlanningPhase();
    }

    private void nextActionPhase(){
        if(turn < game.getNumberOfPlayers() && game.winner().equals(Game.NO_NICKNAME)){
            moveStudent(game.getPlayerNumber(game.getActionOrder()[turn]));
        }
        else{
            turn = 0;
            game.createNextPlanningOrder();
            playTurn();
        }
    }

    private void moveStudent(Player player){
        if(game.studentsLeftToMove(player) > 0){
            view.showStudentsLeftToMovePlayer(player, player.getNumberOfMovedStudents());
        }
        else {
            game.updateProfessorsOwnership(player);
            view.showNewProfessorsOwnership();
            moveMotherNature(player);
        }
    }

    public void selectedStudentToMove(int student, int destination, int indexDestination){
        Player pl = game.getPlayerNumber(game.getActionOrder()[turn]);
        if(destination == Game.MOVE_TO_ISLAND){
            //TODO display movement of student directly in view
            game.playerMoveStudentToIsland(pl, student, indexDestination);
        }
        else if(destination == Game.MOVE_TO_DINING_HALL){
            //TODO display movement of student directly in view
            game.playerMoveStudentToDiningHall(pl, student);
        }
        moveStudent(pl);
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
}
