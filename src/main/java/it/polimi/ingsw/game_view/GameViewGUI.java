package it.polimi.ingsw.game_view;

import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.GameBoardAdvanced;
import it.polimi.ingsw.game_view.controller.GameBoardController;
import it.polimi.ingsw.game_view.controller.LoginController;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.controller.InitialPageController;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class GameViewGUI extends Application implements GameViewClient{
    private final boolean testing = false;

    private static final String pathInitialPage = "fxml/Login.fxml";
    private static final String joiningActionPage = "fxml/Menu.fxml";
    private ClientMessageObserverHandler msgHandler;
    private LoginController controllerInitial;
    private Client client;
    private Stage stage;
    private GameBoardController controllerGameBoard;
    Parent root;


    @Override
    public void start(Stage stage){
        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource(pathInitialPage)));
            root = loader.load();
            this.controllerInitial = loader.getController();
            this.stage = new Stage();
            this.stage.initStyle(StageStyle.UNDECORATED);
            this.stage.setTitle("Eriantys");
            this.stage.setScene(new Scene(root));
            this.stage.setResizable(false);
            this.stage.setWidth(900);
            this.stage.setHeight(550);
            this.stage.setOnCloseRequest(windowEvent -> {
                Platform.exit();
                client.close();
                System.exit(0);
            });
            this.stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(testing){
            testing();
        }
        else {
            msgHandler = new ClientMessageObserverHandler(this);
            controllerInitial.setMessageHandler(msgHandler);
        }
    }

    @Override
    public void displayNotYourTurn() {
        Platform.runLater(() -> {
            controllerGameBoard.setComment("NOT YOUR TURN");
        });
    }

    private void testing(){
        Game game = new GameExpertMode(4);
        GameController gameController = new GameController(game);
        gameController.createPlayer("Paolo", DeckType.ELDER);
        gameController.createPlayer("leo", DeckType.KING);
        gameController.createPlayer("fra", DeckType.PIXIE);
        gameController.createPlayer("bro", DeckType.SORCERER);

        gameReady(new GameBoardAdvanced(game));
    }

    @Override
    public void displayErrorMessage(String errorMsg, String errorType, GameBoard boardToUpdate) {
        Platform.runLater(() -> {
            if(client.getName().equals(boardToUpdate.getCurrentlyPlaying())) {
                controllerGameBoard.updateBoard(boardToUpdate);
                controllerGameBoard.setComment(errorMsg);
            }
        });
    }

    @Override
    public void updateBoard(GameBoard board) {
        controllerGameBoard.updateBoard(board);
    }

    @Override
    public void displayYourTurn() {

    }

    @Override
    public void displayOtherPlayerTurn(String otherPlayerName) {
        // TODO: display the currently playing nickname to all the players except the one which is playing.
    }

    @Override
    public void displayExpertMode() {

    }

    @Override
    public void askName() {
        Platform.runLater(() -> controllerInitial.askNameView());
    }

    @Override
    public void reaskName() {
        Platform.runLater(() -> controllerInitial.reaskNameView());
    }

    @Override
    public void askJoiningAction() {
        Platform.runLater(() -> controllerInitial.askJoiningActionView());
    }

    @Override
    public void displayNoLobbiesAvailable() {
        Platform.runLater(() -> controllerInitial.displayNoLobbiesAvailable());
    }

    @Override
    public void askDeck(Object availableDecks) {
        Platform.runLater(() -> {
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            controllerInitial.setBackgroundColor();
            controllerInitial.askDeckView(availableDecks);
        });
    }

    @Override
    public void askGameType() {
        Platform.runLater(() -> controllerInitial.askGameTypeView());
    }


    @Override
    public void askLobbyToJoin(Object listOfLobbyInfos) {
        Platform.runLater(() -> controllerInitial.askLobbyToJoinView(listOfLobbyInfos));
    }

    @Override
    public void askPlayerNumber() {
        Platform.runLater(() -> controllerInitial.askNumberOfPlayerView());
    }

    @Override
    public void displayLobbyJoined(Object lobbyInfos) {
        Platform.runLater(() -> controllerInitial.displayLobbyJoined((LobbyInfo) lobbyInfos));
    }

    @Override
    public void gameReady(GameBoard board) {
        Platform.runLater(() -> {
            Parent root;

            try{
                this.stage.close();
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/gameBoard.fxml")));
                root = loader.load();
                this.controllerGameBoard = loader.getController();
                if(!testing){
                    controllerGameBoard.setClient(client);
                }
                this.stage.setScene(new Scene(root, 1920, 1080));
                //this.stage.setResizable(true);
                this.stage.setMaximized(true);
                //this.stage.setFullScreen(true);
                //this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                //this.stage.sizeToScene();
                this.stage.setOnCloseRequest(windowEvent -> {
                    Platform.exit();
                    System.exit(0);
                });
                this.stage.show();

            } catch (IOException e) {
                Logger.ERROR("Error while opening the game window.", e.getMessage());
            }
            if(testing){
                controllerGameBoard.setClientName("Paolo");
            }
            else {
                controllerGameBoard.setClient(client);
            }

            controllerGameBoard.updateBoard(board);
        });
    }

    @Override
    public void reaskAssistant() {
        controllerGameBoard.makeAssistantCardPlayable();
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

    public ClientMessageObserverHandler getMsgHandler() {
        return msgHandler;
    }
}
