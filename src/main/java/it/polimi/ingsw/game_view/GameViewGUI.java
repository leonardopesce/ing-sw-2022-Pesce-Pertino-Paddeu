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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class GameViewGUI extends Application implements GameViewClient{
    private final boolean testing = false;

    private static final String pathInitialPage = "fxml/Login.fxml";
    private ClientMessageObserverHandler msgHandler;
    private LoginController controllerInitial;
    private Stage stage;
    private GameBoardController controllerGameBoard;
    private FXMLLoader currentLoader;
    private Parent gameBoardRoot;
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
                controllerInitial.getClient().close();
                System.exit(0);
            });
            this.stage.show();
            Platform.runLater(() -> {
                System.out.println("init music");
                try {
                    Media sound = new Media(getClass().getResource("/music/Wii_Sports.mp3").toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(0.02);
                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();
                    });

                    mediaPlayer.play();

                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public void onPlayerDisconnection(String playerWhoMadeTheLobbyClose) {
        Platform.runLater(() -> {
            Parent root;

            try{
                this.stage.close();
                this.stage = new Stage();
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Login.fxml")));
                root = loader.load();
                this.controllerInitial = loader.getController();
                this.stage.setTitle("Eriantys");
                this.stage.initStyle(StageStyle.UNDECORATED);
                this.stage.setScene(new Scene(root));
                this.stage.setFullScreen(false);
                this.stage.setWidth(900);
                this.stage.setHeight(550);
                this.stage.show();
                controllerInitial.setOnDisconnection(playerWhoMadeTheLobbyClose);
                controllerInitial.setMessageHandler(msgHandler);
            } catch (IOException e) {
                Logger.ERROR("Error while opening the launcher window.", e.getMessage());
            }
        });
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
            if(controllerInitial.getClient().getName().equals(boardToUpdate.getCurrentlyPlaying())) {
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
    public void displayIsChoosingDeckType(Object playerNameWhoIsChosingTheDeck) {
        Platform.runLater(() -> controllerInitial.displayOtherPlayerIsChoosingHisDeckType((String) playerNameWhoIsChosingTheDeck));
    }

    @Override
    public void askDeck(Object availableDecks) {
        Platform.runLater(() -> { controllerInitial.askDeckView((List<DeckType>) availableDecks); });
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
            this.stage.close();
            Group videoRoot = new Group();
            Media introductionVideo = null;
            try {
                introductionVideo = new Media(Objects.requireNonNull(getClass().getResource("/video/startingAnimation.mp4")).toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            MediaPlayer mediaPlayer = new MediaPlayer(introductionVideo);
            mediaPlayer.setAutoPlay(true);
            Platform.runLater(() -> {
                currentLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/gameBoard.fxml")));
                try {
                    gameBoardRoot = currentLoader.load();
                } catch (IOException e) {
                    Logger.ERROR("Error while opening the game window.", e.getMessage());
                }
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                this.controllerGameBoard = currentLoader.getController();
                if (!testing) {
                    controllerGameBoard.setClient(controllerInitial.getClient());
                }
                this.stage.setScene(new Scene(gameBoardRoot, 1920, 1080));
                this.stage.setMaximized(true);
                this.stage.setOnCloseRequest(windowEvent -> {
                    Platform.exit();
                    System.exit(0);
                });
                this.stage.show();
                if (testing) {
                    controllerGameBoard.setClientName("Paolo");
                } else {
                    controllerGameBoard.setClient(controllerInitial.getClient());
                }

                controllerGameBoard.updateBoard(board);
            });

            MediaView mediaView = new MediaView(mediaPlayer);

            videoRoot.getChildren().add(mediaView);
            this.stage = new Stage();
            this.stage.initStyle(StageStyle.UNDECORATED);
            this.stage.setResizable(false);
            this.stage.setMaximized(true);
            stage.setScene(new Scene(videoRoot, 1920, 1080));
            stage.show();
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
        return controllerInitial.getClient();
    }

    public ClientMessageObserverHandler getMsgHandler() {
        return msgHandler;
    }
}
