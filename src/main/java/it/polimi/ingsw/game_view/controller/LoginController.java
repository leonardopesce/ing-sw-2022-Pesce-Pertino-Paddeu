package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.GAME_TYPE_INFO;
import static javafx.scene.paint.Color.SKYBLUE;

public class LoginController implements Initializable {
    private Client client;
    @FXML
    private AnchorPane parent;
    @FXML
    private Pane content;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Logger.INFO("Launcher inizializzato");
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
}
