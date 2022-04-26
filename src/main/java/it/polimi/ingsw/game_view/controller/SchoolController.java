package it.polimi.ingsw.game_view.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SchoolController implements Initializable {
    @FXML
    Button entranceButton, diningHallButton, entranceButton1;
    @FXML
    ImageView schoolImage;
    @FXML
    StackPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        schoolImage.fitWidthProperty().bind(mainPane.widthProperty());
        schoolImage.fitHeightProperty().bind(mainPane.heightProperty());
    }

    public StackPane getMainPane() {
        return mainPane;
    }

    public ImageView getSchoolImage() {
        return schoolImage;
    }
}
