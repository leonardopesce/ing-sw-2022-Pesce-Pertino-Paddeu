package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    private String clientName;
    private boolean firstTime = true;
    private int showedBoard = 0;
    final RotateTransition rotateTransition = new RotateTransition();
    private List<Button> playerBoardButtons = new ArrayList<>();
    @FXML
    private Button player1Board, player2Board, player3Board, player4Board;
    @FXML
    private StackPane mainPane;
    @FXML
    private RotatingBoardController rotatingBoardController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBoardButtons.addAll(Arrays.asList(player1Board, player2Board, player3Board, player4Board));
        mainPane.setBackground(new Background(new BackgroundImage(new Image("img/table.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));

        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setCycleCount(1);
        rotateTransition.setDuration(Duration.millis(1500));
        rotateTransition.setAutoReverse(false);
        rotateTransition.setNode(rotatingBoardController.getPane());
    }

    public void updateBoard(GameBoard board){
        if(firstTime){
            firstTime = false;
            playerBoardButtons.get(showedBoard).setDisable(true);
            for(int i = 0; i < 4; i++){
                if(i < board.getNames().size() + 4){
                    playerBoardButtons.get(i).setText(board.getNames().get(i));
                    int finalI = i;
                    playerBoardButtons.get(i).setOnAction(ActionEvent -> {
                        synchronized (rotateTransition){
                            rotateTransition.setByAngle(getDegreeTurn(finalI));
                            rotateTransition.play();
                            playerBoardButtons.get(showedBoard).setDisable(false);
                            showedBoard = finalI;
                            playerBoardButtons.get(showedBoard).setDisable(true);
                            playerBoardButtons.get(showedBoard).setDisable(true);
                        }
                    });
                }
                else {
                    playerBoardButtons.get(i).setDisable(true);
                    playerBoardButtons.get(i).setVisible(false);
                }
            }

        }
        rotatingBoardController.update(board);
    }


    private int getDegreeTurn(int finalPos){
        return Math.floorMod(finalPos - showedBoard, 4) == 3 ? 90 : Math.floorMod(finalPos - showedBoard, 4) == 2 ? 180 : -90;
    }
}