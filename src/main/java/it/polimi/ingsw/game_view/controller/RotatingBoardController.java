package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class RotatingBoardController implements Initializable {
    private ArrayList<PlayerBoardController> playersBoardController = new ArrayList<>();
    @FXML
    private AnchorPane pane;
    @FXML
    private PlayerBoardController player1Controller, player2Controller, player3Controller, player4Controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersBoardController.addAll(Arrays.asList(player1Controller, player2Controller, player3Controller, player4Controller));
    }

    public void update(GameBoard gameBoard){
        for(int i = 0; i < gameBoard.getNames().size(); i++){
            //playersBoardController.get(i).bindDimension(pane.widthProperty(), pane.heightProperty());
            playersBoardController.get(i).setName(gameBoard.getNames().get(i));
            playersBoardController.get(i).setSchool(gameBoard.getSchools().get(i));

        }
    }

    public AnchorPane getPane() {
        return pane;
    }
}
