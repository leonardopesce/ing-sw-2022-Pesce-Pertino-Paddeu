package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_view.board.AdvancedCardBoard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;

public class AdvancedCardController implements Initializable {
    private final List<ImageView> objects = new ArrayList<>();
    private AdvancedCharacterType type;
    private int cost = 0;
    private int obj = 0;
    private
    @FXML
    Label coinLabel;
    @FXML
    ImageView cardImage, coinImage, obj0, obj1, obj2, obj3, obj4, obj5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objects.addAll(Arrays.asList(obj0, obj1, obj2, obj3, obj4, obj5));

    }

    public void update(AdvancedCardBoard card){
        type = card.getType();
        cardImage.setImage(new Image("/img/advanced/" + type.getCardName() + ".jpg"));
        cardImage.setEffect(null);
        if(type == PRINCESS || type == MONK || type == JESTER){
            obj = card.getStudentsSize();
            for(int i = 0; i < obj; i++){
                objects.get(i).setImage(new Image("img/wooden_pieces/student_" + card.getStudents().get(i) + ".png"));
                objects.get(i).setEffect(null);
            }
        }
        else if(type == MERCHANT || type == LANDLORD){
            obj = 5;
            for(int i = 0; i < obj; i++){
                objects.get(i).setImage(new Image("img/wooden_pieces/student_" + ColorCharacter.values()[i] + ".png"));
                objects.get(i).setEffect(null);
            }
        }
        else if(type == HEALER){
            obj = card.getDenyCard();
            Image img = new Image("img/wooden_pieces/deny_island_icon.png");
            for(int i = 0; i < objects.size(); i++){
                if(i < obj) {
                    objects.get(i).setImage(img);
                }
                else {
                    objects.get(i).setImage(null);
                }
            }

        }

        cost = card.getCost();
        coinLabel.setVisible(card.getCost() > card.getType().getCardCost());


    }

    public AdvancedCharacterType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public void hideObjects(){
        for(ImageView obj: objects){
            obj.setVisible(false);
        }
    }

    public List<ImageView> getObjects() {
        return objects;
    }

    public int getObj() {
        return obj;
    }

    public int getSelectedItem(){
        return (int) objects.stream().filter(image -> image.getEffect() != null).count();
    }

    public ImageView getCardImage() {
        return cardImage;
    }

    public void playEffect(GameBoardController gameBoard){
        if(type == BARTENDER || type == POSTMAN || type == CENTAURUS || type == KNIGHT){
            gameBoard.setPlayingAdvancedCard(1);
            if(type == POSTMAN){
                gameBoard.setPlayingAdvancedCard(POSTMAN.ordinal());
            }
            gameBoard.calculateNextAction();
        }
        else if(type == MONK || type == MERCHANT || type == LANDLORD || type == PRINCESS){
            for(int i = 0; i < obj; i++){
                gameBoard.setHoverEffect(objects.get(i), objects.get(i).getFitWidth());
                int finalI = i;
                objects.get(i).setOnMouseClicked(a -> {
                    gameBoard.addActionValue(finalI);
                    gameBoard.setPlayingAdvancedCard(1);
                    if(type == PRINCESS){
                        gameBoard.setPlayingAdvancedCard(PRINCESS.ordinal());
                    }
                    if(type == MONK){
                        gameBoard.makeVisibleIslandsSelectable();
                    }
                    else{
                        gameBoard.calculateNextAction();
                    }
                    for(int  k= 0; k < obj; k++){
                        if(k != finalI){
                            gameBoard.resetHoverEffect(objects.get(k));
                            objects.get(k).setOnMouseClicked(null);
                        }
                    }
                    gameBoard.resetHoverEffect(objects.get(finalI));
                    objects.get(finalI).setOnMouseClicked(null);
                });
            }
        }
        else if(type == FLAGMAN || type == HEALER){
            gameBoard.setPlayingAdvancedCard(1);
            gameBoard.makeVisibleIslandsSelectable();
        }
        else if(type == BARD){
            gameBoard.setPlayingAdvancedCard(BARD.ordinal());
            gameBoard.setComment("Select 1 or 2 student from your entrance and select 1 or 2 table to exchange them with (player and table must be in the same number, first select all the student from the entrance, then select the tables)");
            gameBoard.makeStudentEntranceSelectable();

        }
        else if(type == JESTER){
            gameBoard.setPlayingAdvancedCard(JESTER.ordinal());
            gameBoard.setComment("Select up to 1, 2 or 3 student from this card and replace them with the same number of student from your entrance (first select the student from the card then the student in your entrance)");
            for(int i = 0; i < obj; i++) {
                if(objects.get(i).getEffect() == null && gameBoard.getActionValues().size() < 4) {
                    gameBoard.setHoverEffect(objects.get(i), objects.get(i).getFitWidth());
                    int finalI = i;
                    objects.get(i).setOnMouseClicked(a -> {
                        gameBoard.addActionValue(finalI);
                        if(gameBoard.getActionValues().size() >= 2){
                            gameBoard.makeStudentEntranceSelectable();
                        }
                        for(ImageView image: objects){
                            if(!objects.get(finalI).equals(image)){
                                gameBoard.resetHoverEffect(image);
                                image.setOnMouseClicked(null);

                            }
                        }
                        gameBoard.resetHoverEffect(objects.get(finalI));
                        playEffect(gameBoard);
                        objects.get(finalI).setOnMouseClicked(null);

                    });
                }
            }
        }

    }
}
