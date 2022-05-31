package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
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

/**
 * Controller for the island fxml
 */
public class IslandController implements Initializable {
    private final List<ImageView> studentImage = new ArrayList<>();
    private final List<Label> studentNumber = new ArrayList<>();
    private int ID;
    private final List<ImageView> islandsImage = new ArrayList<>();
    @FXML
    private StackPane mainPane;
    @FXML
    private ImageView island, motherNature, redStudent, yellowStudent, pinkStudent, greenStudent, blueStudent, towerImage, denyImage;
    @FXML
    private Label studentsNumberRed, studentsNumberYellow, studentsNumberPink, studentsNumberGreen, studentsNumberBlue, towerNumber, denyLabel;

    /**
     * Initializer method initialize all the object used by the class: tower, students, mother nature and deny tile
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
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

    /**
     * Getter for the ID of the island
     * @return an int representing the DI of the island
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter for the ID of the Island
     * @param ID an int for the desired ID of the island to be set
     */
    public void setID(int ID) {
        this.ID = ID;
        Platform.runLater(() -> island.setImage(islandsImage.get(ID % 3).getImage()));
    }

    /**
     * Getter for the Image of this Island
     * @return an ImageVIew of this Island
     */
    public ImageView getIsland() {
        return island;
    }

    /**
     * Hides the island and all of its objects
     */
    public void hide(){
        mainPane.setVisible(false);
    }

    /**
     * Getter for the visible condition of ths island
     * @return false if the island is invisible true if the island is visible
     */
    public boolean isVisible(){
        return mainPane.isVisible();
    }

    /**
     * Shows the island (from hidden to visible)
     */
    public void unHide(){
        mainPane.setVisible(true);
    }

    /**
     * Update the displayed content no the island such as tower number and color, deny tile, students number and color and mother nature
     * @param islandBoard representing the state of the island to show
     */
    public void update(IslandBoard islandBoard){
        // Setup mother nature
        motherNature.setVisible(islandBoard.hasMotherNature());
        // Setup tower
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
        // Setup deny tile
        if(islandBoard.getDeniedCounter() > 0){
            denyLabel.setText(String.valueOf(islandBoard.getDeniedCounter()));
            denyImage.setVisible(true);
            denyLabel.setVisible(true);
        }
        else {
            denyImage.setVisible(false);
            denyLabel.setVisible(false);
        }
        // Setup students
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
