package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the School fxml
 */
public class SchoolController implements Initializable {
    private final List<ImageView> entranceStudents = new ArrayList<>();
    private final List<ImageView> teachersImage = new ArrayList<>();
    private final List<Circle> towersAvailable = new ArrayList<>();
    private final List<HBox> tables = new ArrayList<>();
    @FXML
    private HBox greenTable, redTable, yellowTable, pinkTable, blueTable;
    @FXML
    private ImageView st0, st1, st2, st3, st4, st5, st6, st7, st8, greenProfessor, redProfessor, yellowProfessor, pinkProfessor, blueProfessor, moneyImage;
    @FXML
    private Circle t0, t1, t2, t3, t4, t5, t6, t7;
    @FXML
    private ImageView schoolImage;
    @FXML
    private StackPane mainPane;
    @FXML
    private GridPane diningHall;
    @FXML
    private Label moneyLabel;

    /**
     * Initialize function, initializes all the variable used by the class
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teachersImage.addAll(Arrays.asList(greenProfessor, redProfessor, yellowProfessor, pinkProfessor, blueProfessor));
        tables.addAll(Arrays.asList(greenTable, redTable, yellowTable, pinkTable, blueTable));
        schoolImage.fitWidthProperty().bind(mainPane.widthProperty());
        schoolImage.fitHeightProperty().bind(mainPane.heightProperty());
        schoolImage.setPreserveRatio(false);
        entranceStudents.addAll(Arrays.asList(st0, st1, st2, st3, st4, st5, st6, st7, st8));

        towersAvailable.addAll(Arrays.asList(t0, t1, t2, t3, t4, t5, t6, t7));
        for(Circle tower: towersAvailable){
            tower.setFill(Color.TRANSPARENT);
        }
    }

    /**
     * Getter of the diningHall students and teachers
     * @return a GridPane containing all the students and teachers in the dining hall
     */
    public GridPane getDiningHall() {
        return diningHall;
    }

    /**
     * Setter for the students in the entrance
     * @param students a list of colors representing the colors of the students in the entrance
     */
    public void setEntranceStudents(List<ColorCharacter> students){
        for(int i = 0; i < entranceStudents.size(); i++){
            entranceStudents.get(i).setEffect(null);
            if(i < students.size()){
                entranceStudents.get(i).setImage(new Image("img/wooden_pieces/student_" + students.get(i).toString() + ".png"));
            }
            else {
                entranceStudents.get(i).setImage(null);
            }
        }
    }

    /**
     * Getter for the student's tables in the dining hall
     * @return A list of HBox each contains a number of ImageView based on the number of player in the tables
     */
    public List<HBox> getTables() {
        return tables;
    }

    /**
     * Getter for the students in the entrance
     * @return a list of ImageView representing the image of the student in the entrance
     */
    public List<ImageView> getEntranceStudents() {
        return entranceStudents;
    }

    /**
     * Setter for the teacher's table in the dining hall
     * @param teachers a list of color representing the color of the teachers in the dining hall
     */
    public void setTeachersImage(List<ColorCharacter> teachers){
        for(int i = 0; i < teachersImage.size(); i++){
            int index = teachers.stream().map(ColorCharacter::getTableOrder).toList().indexOf(i);
            teachersImage.get(i).setImage(index == - 1 ? null : new Image("img/wooden_pieces/teacher_" + teachers.get(index) + ".png"));
        }
    }

    /**
     * A setter for the available tower in the School
     * @param numOfTowers an int representing the number of tower present in the school
     * @param color the color of the tower in the school
     */
    public void setTowersAvailable(int numOfTowers, ColorTower color){
        for(int i = 0; i < towersAvailable.size(); i++){
            if(i < numOfTowers){
                towersAvailable.get(i).setFill(ColorTower.getPaint(color));
                towersAvailable.get(i).setStroke(Color.BLACK);
            }
            else {
                towersAvailable.get(i).setFill(Color.TRANSPARENT);
                towersAvailable.get(i).setStroke(Color.TRANSPARENT);
            }
        }
    }

    /**
     * Setter for the money available to the player controlling this school
     * @param coins an int representing the number of coin available in the school
     */
    public void setMoneyAvailable(int coins){
        moneyImage.setVisible(true);
        moneyLabel.setVisible(true);
        moneyLabel.setText(String.valueOf(coins));
    }
}
