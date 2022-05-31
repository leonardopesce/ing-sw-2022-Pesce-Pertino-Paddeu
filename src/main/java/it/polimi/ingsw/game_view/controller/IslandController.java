package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_view.board.IslandBoard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class IslandController implements Initializable {
    private final List<ImageView> studentImage = new ArrayList<>();
    private final List<Label> studentNumber = new ArrayList<>();
    private int ID;
    private List<ImageView> islandsImage = new ArrayList<>();
    @FXML
    private StackPane mainPane;
    @FXML
    private ImageView island, motherNature, redStudent, yellowStudent, pinkStudent, greenStudent, blueStudent, towerImage, denyImage;
    @FXML
    private Label studentsNumberRed, studentsNumberYellow, studentsNumberPink, studentsNumberGreen, studentsNumberBlue, towerNumber, denyLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 1; i < 4; i++){
            islandsImage.add(new ImageView(new Image("/img/wooden_pieces/island" + i + ".png")));
        }
        studentImage.addAll(Arrays.asList(greenStudent, redStudent, yellowStudent, pinkStudent, blueStudent));
        studentNumber.addAll(Arrays.asList(studentsNumberGreen, studentsNumberRed, studentsNumberYellow,  studentsNumberPink, studentsNumberBlue));
        motherNature.setVisible(false);

        for(ImageView student: studentImage){
            student.setVisible(false);
        }
        for(Label student: studentNumber){
            student.setVisible(false);
        }
        towerImage.setVisible(false);
        towerNumber.setVisible(false);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
        Platform.runLater(() -> island.setImage(islandsImage.get(ID % 3).getImage()));
    }

    public ImageView getIsland() {
        return island;
    }

    public void hide(){
        mainPane.setVisible(false);
    }

    public boolean isVisible(){
        return mainPane.isVisible();
    }

    public void unHide(){
        mainPane.setVisible(true);
    }

    public void update(IslandBoard islandBoard){
        if(islandBoard.hasMotherNature()){
            motherNature.setVisible(true);
        }
        else {
            motherNature.setVisible(false);
        }

        //TODO add deny icon

        if(islandBoard.getTowerNumber() > 0){
            towerImage.setVisible(true);
            towerNumber.setVisible(true);
            towerNumber.setText(String.valueOf(islandBoard.getTowerNumber()));
            towerImage.setImage(new Image("img/wooden_pieces/" + islandBoard.getTowerColor().toString().toLowerCase() + "_tower.png"));
        }
        else {
            towerImage.setVisible(false);
            towerNumber.setVisible(false);
        }

        if(islandBoard.getDeniedCounter() > 0){
            denyLabel.setText(String.valueOf(islandBoard.getDeniedCounter()));
            denyImage.setVisible(true);
            denyLabel.setVisible(true);
        }
        else {
            denyImage.setVisible(false);
            denyLabel.setVisible(false);
        }

        int[] students = {0, 0, 0, 0, 0};
        for(ColorCharacter student: islandBoard.getStudents()){
            students[student.ordinal()]++;
        }

        for(int i = 0; i < students.length; i++){
            if(students[i] > 0){

                studentImage.get(i).setVisible(true);
                studentNumber.get(i).setVisible(true);
                studentNumber.get(i).setText(String.valueOf(students[i]));
            }
            else {
                studentImage.get(i).setVisible(false);
                studentNumber.get(i).setVisible(false);
            }
        }
    }
}
