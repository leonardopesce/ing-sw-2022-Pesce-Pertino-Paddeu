package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerBoardRotatedController extends PlayerBoardController{
    @FXML
    PlayerBoardController playerBoardRotatedController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    @Override
    public void setName(String nickName) {
        playerBoardRotatedController.setName(nickName);
    }

    @Override
    public void setSchool(SchoolBoard school) {
        playerBoardRotatedController.setSchool(school);
    }
}
