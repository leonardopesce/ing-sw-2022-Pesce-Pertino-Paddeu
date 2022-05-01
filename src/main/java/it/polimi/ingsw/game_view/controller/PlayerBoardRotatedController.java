package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.DeckBoard;
import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerBoardRotatedController extends PlayerBoardController{
    @FXML
    PlayerBoardController playerBoardRotatedController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    public PlayerBoardController getPlayerBoardRotatedController() {
        return playerBoardRotatedController;
    }

    @Override
    public void hide(){
        playerBoardRotatedController.hide();
    }

    @Override
    public void setDeckBoard(DeckBoard deckBoard) {
        playerBoardRotatedController.setDeckBoard(deckBoard);
    }

    @Override
    public void setName(String nickName) {
        playerBoardRotatedController.setName(nickName);
    }

    @Override
    public Label getName(){
        return playerBoardRotatedController.getName();
    }

    @Override
    public DeckBoard getDeckBoard() {
        return playerBoardRotatedController.getDeckBoard();
    }



    @Override
    public void setSchool(SchoolBoard school) {
        playerBoardRotatedController.setSchool(school);
    }
}
