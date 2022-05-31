package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller of RotatingBoard fxml
 */
public class RotatingBoardController implements Initializable {
    private final ArrayList<PlayerBoardController> playersBoardController = new ArrayList<>();
    @FXML
    private AnchorPane pane;
    @FXML
    private PlayerBoardController player1Controller, player2Controller, player3Controller, player4Controller;

    /**
     * Initialize function, initializes all the variable used by the class
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersBoardController.addAll(Arrays.asList(player1Controller, player2Controller, player3Controller, player4Controller));
    }

    /**
     * Getter for the board i-th board since the controller for the board are of two different type
     * @param i the number of the board
     * @return the controller of the board
     * @see PlayerBoardController
     */
    public PlayerBoardController getBoardX(int i){
        return i % 2 == 0? playersBoardController.get(i) : ((PlayerBoardRotatedController)playersBoardController.get(i)).getPlayerBoardRotatedController();
    }

    /**
     * Update the Controller of the player board, calls all the method to update and gives them the needed value
     * @param gameBoard the current game board state
     * @see GameBoard
     */
    public void update(GameBoard gameBoard){
        for(int i = 0; i < gameBoard.getNames().size(); i++){
            playersBoardController.get(i).setDeckBoard(gameBoard.getDecks().get(i));
            playersBoardController.get(i).setName(gameBoard.getNames().get(i));
            playersBoardController.get(i).setSchool(gameBoard.getSchools().get(i));
            if(gameBoard.isExpertMode()) {
                playersBoardController.get(i).setMoney(gameBoard.getMoneys().get(i));
            }
        }
    }

    /**
     * Getter of the board with the requested name
     * @param name of the board to retrieve
     * @return the PlayerBoardController of the player with given name
     * @see PlayerBoardController
     */
    public PlayerBoardController getBoardOfPlayerWithName(String name){
        for (PlayerBoardController player: playersBoardController){
            if(Objects.equals(player.getName().getText(), name)){
                return player;
            }
        }
        //not reachable
        return null;
    }

    /**
     * Getter for the Pane in which all the board controller are placed (needed to create the rotating animation)
     * @return the pane containing all the player's boards
     */
    public AnchorPane getPane() {
        return pane;
    }
}
