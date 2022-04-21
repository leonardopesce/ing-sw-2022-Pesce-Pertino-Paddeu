package it.polimi.ingsw.game_view;

import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.observer.Observer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class GameViewGUI extends Application implements GameViewClient{
    private static final String pathInitialPage = "initialPage.fxml";
    private Stage stage;
    private GameBoard board;
    private ClientMessageObserverHandler msgHandler;

    @Override
    public void start(Stage stage) throws Exception {
        Client client = new Client(ClientApp.IP, ClientApp.port);
        new Thread(() -> {
            try {
                client.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        msgHandler = new ClientMessageObserverHandler(this);
        client.addObserver(msgHandler);

        Parent root;

        try{
            /*FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("controller/InitialPageController.java"));
            loader.setControllerFactory(controllerClass -> new InitialPageController(client));
            root = loader.load();*/
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(pathInitialPage)));
            stage = new Stage();
            stage.setWidth(Screen.getPrimary().getBounds().getWidth());
            stage.setHeight(Screen.getPrimary().getBounds().getHeight());
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void displayNotYourTurn() {

    }

    @Override
    public void updateBoard(GameBoard board) {

    }

    @Override
    public void displayYourTurn() {

    }

    @Override
    public void displayExpertMode() {

    }

    @Override
    public void askName() {

    }

    @Override
    public void reaskName() {

    }

    @Override
    public void askDeck(Object availableDecks) {

    }

    @Override
    public void askGameType() {

    }

    @Override
    public void askJoiningAction() {

    }

    @Override
    public void askLobbyToJoin(Object listOfLobbyInfos) {

    }

    @Override
    public void askPlayerNumber() {

    }

    @Override
    public void gameReady(GameBoard board) {

    }

    @Override
    public void reaskAssistant() {

    }

    @Override
    public GameBoard getBoard() {
        return null;
    }

    @Override
    public void setBoard(GameBoard board) {

    }

    @Override
    public Client getClient() {
        return null;
    }

}
