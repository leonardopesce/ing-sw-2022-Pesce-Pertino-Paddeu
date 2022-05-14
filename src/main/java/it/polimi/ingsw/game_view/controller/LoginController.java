package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.NAME_MESSAGE;

public class LoginController implements Initializable {
    private Client client;
    private ClientMessageObserverHandler messageHandler;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private AnchorPane parent;
    @FXML
    private Pane launcherFullPagePaneContent, contentPane;
    @FXML
    private TextField nicknameTextField, serverIpTextField, serverPortTextField;
    @FXML
    private Button loginButton, createLobbyButton, joinLobbyButton;
    @FXML
    private Text loginErrorMessage;
    @FXML
    private ImageView errorLogo;
    @FXML
    private HBox errorBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginErrorMessage.setText("");
        errorLogo.setImage(null);
        errorBox.setVisible(false);
        parent.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        parent.setOnMouseDragged(event -> {
            launcherFullPagePaneContent.getScene().getWindow().setX(event.getScreenX() - xOffset);
            launcherFullPagePaneContent.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });

        loginButton.setOnAction(actionEvent -> {
            errorBox.setVisible(false);
            Logger.INFO("Loggin In...");
            if(!serverPortTextField.getText().equals("") && !serverIpTextField.getText().equals("") && !nicknameTextField.getText().equals("")) {
                client = new Client(serverIpTextField.getText(), Integer.parseInt(serverPortTextField.getText()));
                client.addObserver(messageHandler);
                client.setName(nicknameTextField.getText());
                new Thread(() -> {
                    try {
                        client.run();
                    } catch (IOException e) {
                        Logger.ERROR("Failed to connect to the server with the given ip and port.", e.getMessage());
                        loginErrorMessage.setText("Failed to connect to the server with the given ip and port.");
                        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/connectionError.png"))));
                        errorBox.setVisible(true);
                        client = null;
                    }
                }).start();
            } else {
                errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/invalidAccessInfo.png"))));
                loginErrorMessage.setText("Please, fill all the slots above.");
                errorBox.setVisible(true);
            }
        });

        Logger.INFO("Launcher inizializzato");
    }

    @FXML
    public void exitFromApp(ActionEvent mouseClick) {
        Platform.exit();
        try {
            client.close();
        } catch (Exception ex) {
            Logger.INFO("The client was not created yet, skipping...");
        }
        System.exit(0);
    }

    @FXML
    public void minimizeWindow(ActionEvent mouseClick) {
        ((Stage)launcherFullPagePaneContent.getScene().getWindow()).setIconified(true);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void askNameView(){
        Logger.INFO("Asking name");
        client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, client.getName()));
    }

    public void reaskNameView(){
        Logger.INFO("Reasking name");
        serverIpTextField.getParent().setVisible(false);
        serverPortTextField.getParent().setVisible(false);
        loginErrorMessage.setText("the nickname you have chosen is already taken");
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/nicknameAlreadyTakenError.png"))));
        errorBox.setVisible(true);
        loginButton.setOnAction(actionEvent -> {
            client.setName(nicknameTextField.getText());
            client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, client.getName()));
            errorBox.setVisible(false);
        });
    }

    public void askJoiningActionView(){
        Logger.INFO("Asking joining action");
        contentPane.getChildren().clear();
        ImageView backgroundImg = new ImageView();
        Button createLobbyButton = new Button();
        Button joinLobbyButton = new Button();
        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoPixie.png"))));
        backgroundImg.setFitHeight(755.0);
        backgroundImg.setFitWidth(539.0);
        backgroundImg.setLayoutX(-60.0);
        backgroundImg.setLayoutY(-46.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setPickOnBounds(true);
        createLobbyButton.setLayoutX(20.0);
        createLobbyButton.setLayoutY(305.0);
        createLobbyButton.setMnemonicParsing(false);
        createLobbyButton.setPrefWidth(281.0);
        createLobbyButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        createLobbyButton.setText("Crea una partita");
        createLobbyButton.setTextAlignment(TextAlignment.CENTER);
        joinLobbyButton.setLayoutX(20.0);
        joinLobbyButton.setLayoutY(350.0);
        joinLobbyButton.setMnemonicParsing(false);
        joinLobbyButton.setPrefWidth(281.0);
        joinLobbyButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        joinLobbyButton.setText("Entra in una partita");
        joinLobbyButton.setTextAlignment(TextAlignment.CENTER);

        createLobbyButton.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 0)));
        joinLobbyButton.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 1)));

        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(createLobbyButton);
        contentPane.getChildren().add(joinLobbyButton);
    }

    public void setBackgroundColor(){
        Logger.INFO("Setting bg-color");
    }

    public void askDeckView(Object listAvailableDeck){
        Logger.INFO("Asking deck");
    }


    public void askLobbyToJoinView(Object listOfLobbyInfos){
        Logger.INFO("Asking lobby to join");
    }

    public void askNumberOfPlayerView(){
        Logger.INFO("Asking number of players");
    }

    public void askGameTypeView(){
        Logger.INFO("Asking game type");
    }

    public void setMessageHandler(ClientMessageObserverHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
