package it.polimi.ingsw.game_view;

import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.controller.InitialPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameViewGUI extends Application implements GameViewClient{
    private static final String pathInitialPage = "fxml/initialPage.fxml";
    private GameBoard board;
    private ClientMessageObserverHandler msgHandler;
    private InitialPageController controllerInitial;
    private Client client;
    private Stage stage;



    @Override
    public void start(Stage stage) throws Exception {
        Parent root;

        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource(pathInitialPage)));
            root = loader.load();
            this.controllerInitial = loader.getController();
            this.stage = new Stage();
            this.stage.setTitle("Eriantys");
            this.stage.setScene(new Scene(root, 450, 450));
            this.stage.setResizable(false);
            this.stage.setWidth(630);
            this.stage.setHeight(630);
            this.stage.setOnCloseRequest(windowEvent -> {
                Platform.exit();
                System.exit(0);
            });
            this.stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        client = new Client(ClientApp.IP, ClientApp.port);
        new Thread(() -> {
            try {
                client.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        msgHandler = new ClientMessageObserverHandler(this);
        client.addObserver(msgHandler);
        this.controllerInitial.setClient(client);
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
        System.out.println("Asking name");
        Platform.runLater(() -> controllerInitial.askNameView());
    }

    @Override
    public void reaskName() {
        System.out.println("Reasking name");
        Platform.runLater(() -> controllerInitial.reaskNameView());
    }

    @Override
    public void askDeck(Object availableDecks) {
        Platform.runLater(() -> {
            stage.setResizable(true);
            stage.setMaximized(true);
            controllerInitial.setBackgroundColor();
            controllerInitial.askDeckView(availableDecks);
        });
    }

    @Override
    public void askGameType() {
        System.out.println("Asking Game mode");
        Platform.runLater(() -> controllerInitial.askGameTypeView());
    }

    @Override
    public void askJoiningAction() {
        System.out.println("Asking join");
        Platform.runLater(() -> controllerInitial.askJoiningActionView());
    }

    @Override
    public void askLobbyToJoin(Object listOfLobbyInfos) {
        System.out.println("Asking lobby to join");
        Platform.runLater(() -> controllerInitial.askLobbyToJoinView(listOfLobbyInfos));
    }

    @Override
    public void askPlayerNumber() {
        System.out.println("Asking number of player");
        Platform.runLater(() -> controllerInitial.askNumberOfPlayerView());
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
        return client;
    }

}
