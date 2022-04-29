package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerBoardController implements Initializable {
    @FXML
    private Label nickName;
    @FXML
    private Group playerBoard;
    @FXML
    private SchoolController schoolController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void bindDimension(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height){
        playerBoard.scaleXProperty().bind(width.divide(1920));
        playerBoard.scaleYProperty().bind(height.divide(1080));
    }

    public void setName(String nickName) {
        this.nickName.setText(nickName);
    }

    public void setSchool(SchoolBoard school){
        schoolController.setEntranceStudents(school.getEntrance());
        schoolController.setTowersAvailable(school.getTowers(), school.getTowerColor());
    }

}