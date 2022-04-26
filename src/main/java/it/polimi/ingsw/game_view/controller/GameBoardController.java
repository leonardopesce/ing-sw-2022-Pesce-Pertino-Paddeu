package it.polimi.ingsw.game_view.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    GridPane grid;
    @FXML
    Label currentPlayerName;
    @FXML
    SchoolController currentPlayerBoardController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //currentPlayerBoardController.getMainPane().prefWidthProperty().bind(grid.getColumnConstraints().get(1).prefWidthProperty().multiply(0.5));
        //currentPlayerBoardController.getMainPane().prefHeightProperty().bind(grid.getRowConstraints().get(2).prefHeightProperty().multiply(0.5));


    }
}
