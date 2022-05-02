package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

public class SchoolController implements Initializable {
    private final ArrayList<ImageView> entranceStudents = new ArrayList<>();
    private final ArrayList<Circle> towersAvailable = new ArrayList<>();
    private final ArrayList<HBox> tables = new ArrayList<>();
    @FXML
    HBox greenTable, redTable, yellowTable, pinkTable, blueTable;
    @FXML
    ImageView st0, st1, st2, st3, st4, st5, st6, st7, st8;
    @FXML
    Circle t0, t1, t2, t3, t4, t5, t6, t7;
    @FXML
    ImageView schoolImage;
    @FXML
    StackPane mainPane;
    @FXML
    GridPane entrance, diningHall;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public GridPane getDiningHall() {
        return diningHall;
    }

    public void setEntranceStudents(List<ColorCharacter> students){
        for(int i = 0; i < entranceStudents.size(); i++){
            if(i < students.size()){
                entranceStudents.get(i).setImage(new Image("img/wooden_pieces/student_" + students.get(i).toString() + ".png"));
            }
            else {
                entranceStudents.get(i).setImage(null);
            }
        }
    }

    public ArrayList<HBox> getTables() {
        return tables;
    }

    public ArrayList<ImageView> getEntranceStudents() {
        return entranceStudents;
    }

    public void setTowersAvailable(int numOfTowers, ColorTower color){
        for(int i = 0; i < towersAvailable.size(); i++){
            if(i < numOfTowers){
                towersAvailable.get(i).setFill(ColorTower.getPaint(color));
            }
        }
    }
}
