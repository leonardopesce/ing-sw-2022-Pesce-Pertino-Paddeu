package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_view.board.IslandBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class IslandController implements Initializable {
    private final List<ImageView> studentImage = new ArrayList<>();
    private final List<Label> studentNumber = new ArrayList<>();
    @FXML
    private ImageView island, motherNature, redStudent, yellowStudent, pinkStudent, greenStudent, blueStudent, towerImage;
    @FXML
    private Label studentsNumberRed, studentsNumberYellow, studentsNumberPink, studentsNumberGreen, studentsNumberBlue, towerNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentImage.addAll(Arrays.asList(greenStudent, blueStudent, redStudent, pinkStudent, yellowStudent));
        studentNumber.addAll(Arrays.asList(studentsNumberGreen, studentsNumberBlue, studentsNumberRed, studentsNumberPink, studentsNumberYellow));
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

    public ImageView getIsland() {
        return island;
    }

    public void hide(){
        island.setVisible(false);
    }

    public boolean isVisible(){
        return island.isVisible();
    }

    public void unHide(){
        island.setVisible(true);
    }

    public void update(IslandBoard islandBoard){
        if(islandBoard.hasMotherNature()){
            motherNature.setVisible(true);
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
