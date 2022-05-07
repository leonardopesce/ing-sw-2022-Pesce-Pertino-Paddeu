package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
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
        if(type == PRINCESS || type == MONK || type == JESTER){
            obj = card.getStudentsSize();
            for(int i = 0; i < obj; i++){
                objects.get(i).setImage(new Image("img/wooden_pieces/student_" + card.getStudents().get(i) + ".png"));
            }
        }
        else if(type == HEALER){
            obj = card.getDenyCard();
            Image img = new Image("img/wooden_pieces/deny_island_icon.png");
            for(int i = 0; i < obj; i++){
                objects.get(i).setImage(img);
            }
        }

        coinLabel.setVisible(card.getCost() > card.getType().getCardCost());


    }

    public void hideObjects(){
        for(ImageView obj: objects){
            obj.setVisible(false);
        }
    }

}
