package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_view.board.DeckBoard;
import it.polimi.ingsw.game_view.board.SchoolBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller of the PlayerBoard fxml
 */
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

    /**
     * Initialize function required by Interface
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Hides player board in case the game is at 2 or 3 players
     */
    public void hide(){
        playerBoard.setVisible(false);
    }

    /**
     * Getter for the deck board of the player
     * @return the DeckBoard of the player
     * @see DeckBoard
     */
    public DeckBoard getDeckBoard() {
        return deckBoard;
    }

    /**
     * Setter for the deck board sets up the last played card
     * @param deckBoard of type DeckBoard to get the last played card (if not null)
     * @see DeckBoard
     */
    public void setDeckBoard(DeckBoard deckBoard) {
        this.deckBoard = deckBoard;
        if(deckBoard.getDiscardedCard() != null){
            lastPlayedCard.setImage(new Image("img/assistant/Assistente (" + (deckBoard.getDiscardedCard().getType().getCardTurnValue()) + ").png"));
        }
    }

    /**
     * Getter for the name of this board
     * @return a String containing the name of the player associated to this board
     */
    public Label getName() {
        return nickName;
    }

    /**
     * Setter for the name of this board
     * @param nickName as String of the player at which you want to map the current board
     */
    public void setName(String nickName) {
        this.nickName.setText(nickName);
    }

    /**
     * Set up the school of this board
     * @param school containing the value of the school or the player to represent in the GUI
     * @see SchoolBoard
     */
    public void setSchool(SchoolBoard school){
        schoolController.setEntranceStudents(school.getEntrance());
        schoolController.setTowersAvailable(school.getTowers(), school.getTowerColor());
        schoolController.setTeachersImage(school.getTeachers());
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

    }

    /**
     * Sets the money available for the player
     * @param coins the number of coins available to the player
     */
    public void setMoney(int coins){
        schoolController.setMoneyAvailable(coins);
    }

    /**
     * Getter for the School Controller
     * @return the SchoolController associated to this board
     * @see SchoolController
     */
    public SchoolController getSchool() {
        return schoolController;
    }
}
