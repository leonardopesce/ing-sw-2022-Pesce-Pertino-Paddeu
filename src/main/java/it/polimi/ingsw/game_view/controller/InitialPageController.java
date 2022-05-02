package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.utils.LobbyInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;
import static javafx.scene.paint.Color.SKYBLUE;

public class InitialPageController implements Initializable {
    private Client client;
    private final Label errorTextField = new Label();
    private VBox currentBox;
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
        currentBox = createQuestionBox(2.5, 3);

        currentBox.getChildren().add(createTextLabel("Insert a NickName", FontWeight.BOLD, Color.BLACK, 20));

        TextField response = new TextField();
        response.setMaxWidth(mainPane.widthProperty().divide(3).get());
        currentBox.getChildren().add(response);

        currentBox.getChildren().add(errorTextField);
        errorTextField.setText("");

        Button send = new Button();
        send.setText("Choose name");
        send.setOnAction(actionEvent -> {
            client.setName(response.getText());
            client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, response.getText()));
        });
        currentBox.getChildren().add(send);

        mainPane.getChildren().add(currentBox);
    }

    public void reaskNameView(){
        errorTextField.setTextFill(Color.RED);
        errorTextField.setText("nickname already chosen");
    }

    public void setBackgroundColor(){
        mainPane.setBackground(new Background(new BackgroundFill(SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void askDeckView(Object listAvailableDeck){
        mainPane.getChildren().clear();
        errorTextField.setText("");
        currentBox = createQuestionBox(3, 1.2);

        currentBox.getChildren().add(createTextLabel("Which deck do you want to choose?", FontWeight.BOLD, Color.BLACK, 20));

        GridPane cardSelectionPane = new GridPane();
        cardSelectionPane.setHgap(10);
        cardSelectionPane.setVgap(10);
        cardSelectionPane.setAlignment(Pos.CENTER);

        for(int i = 0; i < ((List<?>)listAvailableDeck).size(); i++){
            Button button = new Button();
            DeckType card = ((DeckType)((List<?>)listAvailableDeck).get(i));
            button.setPrefSize(mainPane.widthProperty().divide(7).get(), mainPane.heightProperty().divide(3).get());
            button.setStyle("-fx-background-radius: 10; -fx-background-image: url(" + card.getPath() + "); -fx-background-size: 100% 100%; -fx-background-position: center");
            button.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(DECK_TYPE_MESSAGE, card)));
            cardSelectionPane.add(button, i < 2 ? i : i % 2, i < 2 ? 0 : 1);
        }

        currentBox.getChildren().add(cardSelectionPane);

        mainPane.getChildren().add(currentBox);
    }

    public void askJoiningActionView(){
        mainPane.getChildren().clear();
        errorTextField.setText("");
        currentBox = createQuestionBox(1.5, 5);

        currentBox.getChildren().add(createTextLabel("What do you want to do?", FontWeight.BOLD, Color.BLACK, 20));
        Button createGame = new Button("Create a game");
        Button joinButton = new Button("Join a game");

        createGame.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 0)));
        joinButton.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 1)));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(createGame, joinButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);

        currentBox.getChildren().add(hBox);

        mainPane.getChildren().add(currentBox);
    }

    public void askLobbyToJoinView(Object listOfLobbyInfos){
        mainPane.getChildren().clear();
        errorTextField.setText("");
        currentBox = createQuestionBox(2.5, 2.5);

        currentBox.getChildren().add(createTextLabel("Choose a Lobby", FontWeight.BOLD, Color.BLACK, 20));

        ListView<LobbyInfo> listView = new ListView<>();
        listView.getItems().addAll((List<LobbyInfo>)listOfLobbyInfos);
        currentBox.getChildren().addAll(listView, errorTextField);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);

        /*Button goBack = new Button("Go back");
        goBack.setOnAction(ActionEvent -> {
            askJoiningActionView();
        });*/

        Button select = new Button("Select");
        select.setOnAction(ActionEvent -> {
            if(listView.getSelectionModel().getSelectedItems().size() == 1){
                LobbyInfo selected = listView.getSelectionModel().getSelectedItems().get(0);
                if(!selected.isFull()){
                    client.asyncWriteToSocket(new CommunicationMessage(LOBBY_TO_JOIN_INFO, selected.getLobbyName()));
                }
                else{
                    errorTextField.setText("Lobby full choose another or go back");
                }
            }
            else {
                errorTextField.setText("Please select a lobby");
            }
        });

        hBox.getChildren().addAll(select);
        currentBox.getChildren().add(hBox);

        mainPane.getChildren().add(currentBox);
    }

    public void askNumberOfPlayerView(){
        mainPane.getChildren().clear();
        errorTextField.setText("");
        currentBox = createQuestionBox(1.5, 5);

        currentBox.getChildren().add(createTextLabel("How many player do you want", FontWeight.BOLD, Color.BLACK, 20));

        Button button2 = new Button("2");
        Button button3 = new Button("3");
        Button button4 = new Button("4");

        button2.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 2)));
        button3.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 3)));
        button4.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 4)));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(button2, button3, button4);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        currentBox.getChildren().add(hBox);

        mainPane.getChildren().add(currentBox);
    }

    public void askGameTypeView(){
        mainPane.getChildren().clear();
        errorTextField.setText("");
        currentBox = createQuestionBox(1.5, 5);

        currentBox.getChildren().add(createTextLabel("Do you want to play in expert mode?", FontWeight.BOLD, Color.BLACK, 20));

        Button yesButton = new Button("No");
        Button noButton = new Button("Yes");

        noButton.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(GAME_TYPE_INFO, false)));
        yesButton.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(GAME_TYPE_INFO, true)));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(noButton, yesButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        currentBox.getChildren().add(hBox);

        mainPane.getChildren().add(currentBox);
    }

    private VBox createQuestionBox(double widthDivider, double heightDivider){
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        box.setBorder(new Border(new BorderStroke(Color.rgb(255, 200, 0), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THICK)));
        box.setBackground(new Background(new BackgroundFill(Color.rgb(255, 230, 130, 0.7), new CornerRadii(15), Insets.EMPTY)));
        box.setMaxSize(mainPane.widthProperty().divide(widthDivider).get(), mainPane.heightProperty().divide(heightDivider).get());
        return box;
    }

    private Label createTextLabel(String text, FontWeight style, Color color, int size){
        Label label = new Label(text);
        label.setFont(Font.font("Arial", style,size));
        label.setTextFill(color);
        return label;
    }
}
