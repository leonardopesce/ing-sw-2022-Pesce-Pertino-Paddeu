package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.GameViewClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

public class InitialPageController implements Initializable {
    private Client client;

    @FXML
    StackPane mainPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Rendering page");
        ImageView img = new ImageView("img/background.png");
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setBackground(new Background(new BackgroundImage(img.getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void askNameView(){
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        box.setBorder(new Border(new BorderStroke(Color.rgb(255, 200, 0), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THICK)));
        box.setBackground(new Background(new BackgroundFill(Color.rgb(255, 230, 130, 0.7), new CornerRadii(15), Insets.EMPTY)));
        box.setMaxSize(mainPane.widthProperty().multiply(0.4).get(), mainPane.heightProperty().divide(4).get());

        Label text = new Label();
        text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,20));
        text.setText(GameViewClient.ASK_NAME_QUESTION);
        box.getChildren().add(text);

        TextField response = new TextField();
        response.setMaxWidth(mainPane.widthProperty().divide(3).get());
        box.getChildren().add(response);

        Button send = new Button();
        send.setText("Choose name");
        send.setOnAction(actionEvent -> {
            client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.ASK_NAME, response.getText()));
        });
        box.getChildren().add(send);

        mainPane.getChildren().add(box);
    }


}
