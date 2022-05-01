package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.IslandBoard;
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
    private List<IslandController> islands = new ArrayList<>();
    @FXML
    ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10;
    @FXML
    private Button player1Board, player2Board, player3Board, player4Board;
    @FXML
    private StackPane mainPane;
    @FXML
    private IslandController island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller;
    @FXML
    private RotatingBoardController rotatingBoardController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBoardButtons.addAll(Arrays.asList(player1Board, player2Board, player3Board, player4Board));
        islands.addAll(Arrays.asList(island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller));
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
        final int ANIMATION_DURATION = 500;

        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            assistant.setTranslateY(assistant.getFitHeight() * 0.8);
            TranslateTransition moveUpEffect = new TranslateTransition(Duration.millis(ANIMATION_DURATION), assistant);
            TranslateTransition moveDownEffect = new TranslateTransition(Duration.millis(ANIMATION_DURATION), assistant);
            int finalI = i;
            assistant.setOnMouseEntered(ActionEvent -> {
                new Thread(() -> {
                    if(!isUp[finalI]){
                        moveUpEffect.setByY(- assistant.getFitHeight() * 0.9);
                        moveUpEffect.play();
                        moveUpEffect.setOnFinished(a -> isUp[finalI] = true);
                    }
                }).start();
            });
            assistant.setOnMouseExited(ActionEvent -> {
                new Thread(() -> {
                    try {
                        Thread.sleep(ANIMATION_DURATION);
                    } catch (InterruptedException e) {
                        // Impossible to reach since the animation duration is ALWAYS > 0
                        e.printStackTrace();
                    }
                    if(isUp[finalI]){
                        moveDownEffect.setByY(assistant.getFitHeight() * 0.9);
                        moveDownEffect.play();
                        moveDownEffect.setOnFinished(a -> isUp[finalI] = false);
                    }
                }).start();
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
                        new Thread(() -> {
                            synchronized (rotateTransition){
                                rotateTransition.setByAngle(getDegreeTurn(finalI));
                                rotateTransition.play();
                                playerBoardButtons.get(showedBoard).setDisable(false);
                                showedBoard = finalI;
                                playerBoardButtons.get(showedBoard).setDisable(true);
                                playerBoardButtons.get(showedBoard).setDisable(true);
                            }
                        }).start();
                    });
                }
                else {
                    playerBoardButtons.get(i).setDisable(true);
                    playerBoardButtons.get(i).setVisible(false);
                }
            }

        }
        rotatingBoardController.update(board);
        for (IslandController island: islands){
            island.hide();
        }

        for(IslandBoard island: board.getTerrain().getIslands()){
            islands.get(island.getID()).unHide();
            islands.get(island.getID()).update(island);
        }
    }


    private int getDegreeTurn(int finalPos){
        return Math.floorMod(finalPos - showedBoard, 4) == 3 ? 90 : Math.floorMod(finalPos - showedBoard, 4) == 2 ? 180 : -90;
    }
}
