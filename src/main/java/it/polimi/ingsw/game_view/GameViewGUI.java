package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
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
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * GUI Application, launched at the beginning of the game and interfaces the message from the server with the GUI controller
 */
public class GameViewGUI extends Application implements GameViewClient{
    private final boolean testing = false;
    private static final String pathInitialPage = "fxml/Login.fxml";
    private ClientMessageObserverHandler msgHandler;
    private LoginController controllerInitial;
    private Stage stage;
    private GameBoardController controllerGameBoard;
    private FXMLLoader currentLoader;
    private Parent gameBoardRoot;
    private MediaView videoMediaView;
    private MediaPlayer soundMediaPlayer;
    private MediaPlayer videoMediaPlayer;
    private Media soundMedia;
    private Media videoMedia;

    /**
     * Start function (first function to be executed in Application.launch()
     * @param stage handled by JavaFX
     */
    @Override
    public void start(Stage stage){
        try{
            setInitialLoginStage();
            loadMusic();
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

    /**
     * Loads the background music
     */
    private void loadMusic(){
        new Thread(() -> {
            try {
                soundMedia = new Media(getClass().getResource("/music/Wii_Sports.mp3").toURI().toString());
                soundMediaPlayer = new MediaPlayer(soundMedia);
                soundMediaPlayer.setVolume(0.05);
                soundMediaPlayer.setOnEndOfMedia(() -> {
                    soundMediaPlayer.seek(Duration.ZERO);
                    soundMediaPlayer.play();
                });

                soundMediaPlayer.setOnReady(() -> soundMediaPlayer.play());

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Set up the initial login screens
     * @throws IOException for close requests gone wrong
     */
    private void setInitialLoginStage() throws IOException {
        preloadGameBoardController();
        Parent root;
        this.stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().getResource(pathInitialPage)));
        root = loader.load();
        this.controllerInitial = loader.getController();
        this.stage.setTitle("Eriantys");
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setScene(new Scene(root));
        this.stage.setResizable(false);
        this.stage.setFullScreen(false);
        this.stage.setWidth(900);
        this.stage.setHeight(550);
        this.stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            try {
                controllerInitial.getClient().close();
            } catch (Exception e) {
                // If the client is not active it's fine anyway.
            }
            System.exit(0);
        });
        this.stage.show();
    }

    /**
     * starts to load the game board controller before is actually needed to make a faster transition from login to game
     */
    private void preloadGameBoardController(){
        //starts loading the page (1) it's heavy (2) avoids a problem where the client can't receive message from the server before it loaded everything
        new Thread(() -> {
            try {
                currentLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/gameBoard.fxml")));
                gameBoardRoot = currentLoader.load();
                controllerGameBoard = currentLoader.getController();
            } catch (IOException e) {
                Logger.ERROR("Error while loading the game window.", e.getMessage());
            }

        }).start();

    }

    /**
     * Function used just for testing porpoise (creates a game without establishing a connection
     */
    private void testing(){
        Game game = new GameExpertMode(4);
        GameController gameController = new GameController(game);
        gameController.createPlayer("Paolo", DeckType.ELDER);
        gameController.createPlayer("leo", DeckType.KING);
        gameController.createPlayer("fra", DeckType.PIXIE);
        gameController.createPlayer("bro", DeckType.SORCERER);
        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameReady(new GameBoardAdvanced(game));
    }

    /**
     * Loads parent given in full screen
     * @param root the parent to load the full screen
     */
    private void loadFullScreen(Parent root) {
        this.stage = new Stage();
        this.stage.setScene(new Scene(root, 1920, 1080));
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setResizable(false);
        this.stage.setMaximized(true);
        this.stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
        this.stage.show();
    }

    @Override
    public void displayErrorMessage(String errorMsg, String errorType, GameBoard boardToUpdate) {
        Platform.runLater(() -> {
            if(controllerInitial.getClient().getName().equals(boardToUpdate.getCurrentlyPlaying())) {
                controllerGameBoard.updateBoard(boardToUpdate);
                controllerGameBoard.setComment(errorMsg);
                controllerGameBoard.setCommentLogo(controllerGameBoard.getErrorLogo());
                controllerGameBoard.setCommentBoxVisible();
            }
        });
    }

    @Override
    public void updateBoard(GameBoard board) {
        controllerGameBoard.updateBoard(board);
    }

    @Override
    public void displayYourTurn() {
        // Already handled in GameBoardController
    }

    @Override
    public void displayNotYourTurn() {
        Platform.runLater(() -> controllerGameBoard.setComment("NOT YOUR TURN"));
    }

    @Override
    public void displayOtherPlayerTurn(String otherPlayerName) {
        // Already handled in GameBoardController
    }

    @Override
    public void displayExpertMode() {
        // Already handled in GameBoardController
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
    public void displayIsChoosingDeckType(Object playerNameWhoIsChosingTheDeck) {
        Platform.runLater(() -> controllerInitial.displayOtherPlayerIsChoosingHisDeckType((String) playerNameWhoIsChosingTheDeck));
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
        Platform.runLater(() -> controllerInitial.askDeckView((List<DeckType>) availableDecks));
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
            try {
                videoMedia = new Media(Objects.requireNonNull(getClass().getResource("/video/startingAnimation.mp4")).toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            videoMediaPlayer = new MediaPlayer(videoMedia);

            videoMediaPlayer.setOnError(() -> gameReady(board));

            videoMediaPlayer.setOnEndOfMedia(() -> {
                Stage previousStage = stage;
                loadFullScreen(gameBoardRoot);
                previousStage.close();
                soundMediaPlayer.play();
                if (testing) {
                    controllerGameBoard.setClientName("Paolo");
                } else {
                    controllerGameBoard.setClient(controllerInitial.getClient());
                }

                controllerGameBoard.updateBoard(board);
            });

            videoMediaView = new MediaView(videoMediaPlayer);
            videoMediaView.setMouseTransparent(true);
            videoRoot.getChildren().add(videoMediaView);
            soundMediaPlayer.pause();
            stage.close();
            loadFullScreen(videoRoot);
            videoMediaPlayer.setAutoPlay(true);
        });
    }

    @Override
    public void reaskAssistant() {
        Platform.runLater(() -> {
            controllerGameBoard.makeAssistantCardPlayable();
            controllerGameBoard.setComment("L'assistente da te scelto è stato già selezionato da un altro giocatore in questo turno. Scegline un altro.");
            controllerGameBoard.setCommentLogo(controllerGameBoard.getErrorLogo());
            controllerGameBoard.setCommentBoxVisible();
        });
    }

    @Override
    public void onPlayerDisconnection(String playerWhoMadeTheLobbyClose) {
        Platform.runLater(() -> {
            if(videoMediaPlayer != null) {
                videoMediaPlayer.stop();
            }
            this.stage.close();
            start(stage);
            controllerInitial.setOnDisconnection(playerWhoMadeTheLobbyClose);
            controllerInitial.setMessageHandler(msgHandler);
        });
    }

    @Override
    public GameBoard getBoard() {
        // used only in CLI
        return null;
    }

    @Override
    public void setBoard(GameBoard board) {
        // used only in CLI
    }

    @Override
    public Client getClient() {
        return controllerInitial.getClient();
    }

    @Override
    public void displayEndGame(CommunicationMessage.MessageType condition) {
        controllerGameBoard.makeEndAnimation(condition);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            Platform.runLater(() -> {
                this.stage.close();
                soundMediaPlayer.stop();
                if(getClient() != null && getClient().isActive()){
                    getClient().close();
                }
                start(this.stage);
            });
        }
    }
}
