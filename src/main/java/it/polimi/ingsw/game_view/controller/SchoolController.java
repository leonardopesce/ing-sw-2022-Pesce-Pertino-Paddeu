package it.polimi.ingsw.game_view.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class SchoolController implements Initializable {
    @FXML
    Button entranceButton, diningHallButton, entranceButton1, student1;
    @FXML
    ImageView schoolImage;
    @FXML
    StackPane mainPane;
    @FXML
    GridPane entrance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        schoolImage.fitWidthProperty().bind(mainPane.widthProperty());
        schoolImage.fitHeightProperty().bind(mainPane.heightProperty());
        schoolImage.setPreserveRatio(false);
        student1.setShape(new Circle(20));
        student1.setScaleShape(false);

    }

    public StackPane getMainPane() {
        return mainPane;
    }

    public ImageView getSchoolImage() {
        return schoolImage;
    }
}
