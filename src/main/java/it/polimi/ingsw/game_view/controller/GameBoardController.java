package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameBoardController implements Initializable {
    private String clientName;
    private boolean firstTime = true;
    private int showedBoard = 0;
    final RotateTransition rotateTransition = new RotateTransition();
    private List<Button> playerBoardButtons = new ArrayList<>();
    private List<ImageView> assistants = new ArrayList<>();
    @FXML
    ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10;
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

        assistants.addAll(Arrays.asList(assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10));
        setAssistantsAction();


        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setCycleCount(1);
        rotateTransition.setDuration(Duration.millis(1500));
        rotateTransition.setAutoReverse(false);
        rotateTransition.setNode(rotatingBoardController.getPane());
    }

    private void setAssistantsAction(){
        final boolean[] isUp = {false, false, false, false, false, false, false, false, false, false};
        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            assistant.setTranslateY(assistant.getFitHeight() * 0.8);
            TranslateTransition moveEffect = new TranslateTransition(Duration.millis(500), assistant);
            int finalI = i;
            assistant.setOnMouseEntered(ActionEvent -> {
                synchronized (moveEffect){
                    if(!isUp[finalI]){
                        moveEffect.setByY(- assistant.getFitHeight() * 0.9);
                        moveEffect.play();
                        moveEffect.setOnFinished(a -> isUp[finalI] = true);
                    }
                }
            });
            assistant.setOnMouseExited(ActionEvent -> {
                synchronized (moveEffect){
                    if(isUp[finalI]){
                        moveEffect.setByY(assistant.getFitHeight() * 0.9);
                        moveEffect.play();
                        moveEffect.setOnFinished(a -> isUp[finalI] = false);
                    }
                }
            });

        }
    }

    public void updateBoard(GameBoard board){
        if(firstTime){
            firstTime = false;
            playerBoardButtons.get(showedBoard).setDisable(true);
            for(int i = 0; i < 4; i++){
                if(i < board.getNames().size() + 4){
                    //playerBoardButtons.get(i).setText(board.getNames().get(i));
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
