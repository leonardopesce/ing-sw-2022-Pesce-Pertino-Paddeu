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
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.NAME_MESSAGE;

public class LoginController implements Initializable {
    private Client client;
    private ClientMessageObserverHandler messageHandler;
    @FXML
    private AnchorPane parent;
    @FXML
    private Pane content;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private TextField serverIpTextField;
    @FXML
    private TextField serverPortTextField;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Logger.INFO("Launcher inizializzato");

        loginButton.setOnAction(actionEvent -> {
            Logger.INFO("Loggin In");
            client = new Client(serverIpTextField.getText(), Integer.parseInt(serverPortTextField.getText()));
            client.setName(nicknameTextField.getText());
            new Thread(() -> {
                try {
                    client.run();
                } catch (IOException e) {
                    Logger.ERROR("Failed to connect to the server with the given ip and port.", e.getMessage());
                }
            }).start();
            client.addObserver(messageHandler);
        });
    }

    @FXML
    public void exitFromApp(ActionEvent mouseClick) {
        Platform.exit();
        client.close();
        System.exit(0);
    }

    @FXML
    public void minimizeWindow(ActionEvent mouseClick) {
        ((Stage)content.getScene().getWindow()).setIconified(true);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void askNameView(){
        Logger.INFO("Asking name");
        client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, nicknameTextField.getText()));
    }

    public void reaskNameView(){
        Logger.INFO("Reasking name");
    }

    public void setBackgroundColor(){
        Logger.INFO("Setting bg-color");
    }

    public void askDeckView(Object listAvailableDeck){
        Logger.INFO("Asking deck");
    }

    public void askJoiningActionView(){
        Logger.INFO("asking joining action");
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
