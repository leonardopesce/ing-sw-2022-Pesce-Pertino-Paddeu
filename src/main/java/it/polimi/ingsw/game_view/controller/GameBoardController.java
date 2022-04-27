package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    private Button rotate;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private RotatingBoardController rotatingBoardController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPane.setBackground(new Background(new BackgroundImage(new Image("img/table.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));
        rotate.setOnAction(ActionEvent -> {
            RotateTransition rotate = new RotateTransition();

            rotate.setAxis(Rotate.Z_AXIS);
            rotate.setByAngle(90);
            rotate.setCycleCount(1);
            rotate.setDuration(Duration.millis(1500));
            rotate.setAutoReverse(false);
            rotate.setNode(rotatingBoardController.getPane());
            rotate.play();

        });
    }

    public void updateBoard(GameBoard board){
        rotatingBoardController.update(board);
    }
}
