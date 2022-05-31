package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.DeckBoard;
import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the PlayerBoard fxml (just a remapping to the PlayerBoardController, needed to create a 90 degree rotated board)
 * @see it.polimi.ingsw.game_view.controller.PlayerBoardController
 */
public class PlayerBoardRotatedController extends PlayerBoardController{
    @FXML
    PlayerBoardController playerBoardRotatedController;

    /**
     * Initialize function required by Interface
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public PlayerBoardController getPlayerBoardRotatedController() {
        return playerBoardRotatedController;
    }

    /**
     * Hides player board in case the game is at 2 or 3 players
     */
    @Override
    public void hide(){
        playerBoardRotatedController.hide();
    }

    /**
     * Setter for the deck board sets up the last played card
     * @param deckBoard of type DeckBoard to get the last played card (if not null)
     * @see DeckBoard
     */
    @Override
    public void setDeckBoard(DeckBoard deckBoard) {
        playerBoardRotatedController.setDeckBoard(deckBoard);
    }

    /**
     * Getter for the School Controller
     * @return the SchoolController associated to this board
     * @see SchoolController
     */
    @Override
    public SchoolController getSchool() {
        return playerBoardRotatedController.getSchool();
    }

    /**
     * Setter for the name of this board
     * @param nickName as String of the player at which you want to map the current board
     */
    @Override
    public void setName(String nickName) {
        playerBoardRotatedController.setName(nickName);
    }

    /**
     * Getter for the name of this board
     * @return a String containing the name of the player associated to this board
     */
    @Override
    public Label getName(){
        return playerBoardRotatedController.getName();
    }

    /**
     * Getter for the deck board of the player
     * @return the DeckBoard of the player
     * @see DeckBoard
     */
    @Override
    public DeckBoard getDeckBoard() {
        return playerBoardRotatedController.getDeckBoard();
    }

    /**
     * Set up the school of this board
     * @param school containing the value of the school or the player to represent in the GUI
     * @see SchoolBoard
     */
    @Override
    public void setSchool(SchoolBoard school) {
        playerBoardRotatedController.setSchool(school);
    }

    /**
     * Sets the money available for the player
     * @param coins the number of coins available to the player
     */
    @Override
    public void setMoney(int coins){
        playerBoardRotatedController.setMoney(coins);
    }
}
