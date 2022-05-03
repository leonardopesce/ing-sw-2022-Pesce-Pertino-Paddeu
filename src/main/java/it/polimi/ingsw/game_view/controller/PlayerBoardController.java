package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_view.board.DeckBoard;
import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class PlayerBoardController implements Initializable {
    private DeckBoard deckBoard;
    @FXML
    private Label nickName;
    @FXML
    private Group playerBoard;
    @FXML
    private ImageView lastPlayedCard;
    @FXML
    private SchoolController schoolController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void bindDimension(ReadOnlyDoubleProperty width, ReadOnlyDoubleProperty height){
        playerBoard.scaleXProperty().bind(width.divide(1920));
        playerBoard.scaleYProperty().bind(height.divide(1080));
    }

    public void hide(){
        playerBoard.setVisible(false);
    }

    public DeckBoard getDeckBoard() {
        return deckBoard;
    }

    public void setDeckBoard(DeckBoard deckBoard) {
        this.deckBoard = deckBoard;
        if(deckBoard.getDiscardedCard() != null){
            lastPlayedCard.setImage(new Image("img/assistant/Assistente (" + (deckBoard.getDiscardedCard().getCardTurnValue()) + ").png"));
        }leo
    }

    public Label getName() {
        return nickName;
    }

    public void setName(String nickName) {
        this.nickName.setText(nickName);
    }

    public void setSchool(SchoolBoard school){
        schoolController.setEntranceStudents(school.getEntrance());
        schoolController.setTowersAvailable(school.getTowers(), school.getTowerColor());
        for(int i = 0; i < school.getTables().length; i++){
            int finalI = i;
            Image student = new Image("img/wooden_pieces/student_" + Arrays.stream(ColorCharacter.values()).reduce((a, b) -> a.getTableOrder() == finalI ? a : b).get() + ".png");
            for(int k = schoolController.getTables().get(i).getChildren().size(); k < school.getTables()[i]; k++){
                schoolController.getTables().get(i).getChildren().add(new ImageView(student));
            }
            for(int k = schoolController.getTables().get(i).getChildren().size(); k > school.getTables()[i]; k--){
                schoolController.getTables().get(i).getChildren().remove(k - 1);
            }

        }

        //TODO add teacher

    }

    public SchoolController getSchool() {
        return schoolController;
    }
}
