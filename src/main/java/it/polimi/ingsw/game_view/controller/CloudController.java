package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.utils.ColorCharacter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the cloud fxml
 */
public class CloudController implements Initializable {
    private final List<ImageView> students = new ArrayList<>();
    @FXML
    private ImageView st0, st1, st2, st3, cloudImage;

    /**
     * Initialize function, initializes all the variable used by the class
     * @param url handled by javafx during loading of fxml
     * @param resourceBundle handled by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        students.addAll(Arrays.asList(st0, st1, st2, st3));
    }

    /**
     * update for the number and color of students on the cloud
     * @param cloud list of element of color
     */
    public void update(List<ColorCharacter> cloud){
        for(ImageView student: students){
            student.setImage(null);
        }
        for(int i = 0; i < cloud.size(); i++){
            students.get(i).setImage(new Image("img/wooden_pieces/student_" + cloud.get(i).toString() + ".png"));
        }
    }

    /**
     * Getter for the Image of the cloud
     * @return a ImageView containing the image of the cloud
     */
    public ImageView getCloudImage() {
        return cloudImage;
    }
}
