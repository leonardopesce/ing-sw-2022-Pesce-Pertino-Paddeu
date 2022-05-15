package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.controller.custom_gui.CustomSwitch;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.NAME_MESSAGE;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.NUMBER_OF_PLAYER_INFO;

public class LoginController implements Initializable {
    private static final String LOGIN_FONT = "Berlin Sans FB";
    private static final String NORMAL_GAME_TXT = "Regole base";
    private static final String EXPERT_GAME_TXT = "Regole per esperti";
    private Client client;
    private ClientMessageObserverHandler messageHandler;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isExpertGame = false;
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
                        loginErrorMessage.setText("Impossibile connettersi al server con indirizzo IP e porta indicati.");
                        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/connectionError.png"))));
                        errorBox.setVisible(true);
                        client = null;
                    }
                }).start();
            } else {
                errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/invalidAccessInfo.png"))));
                loginErrorMessage.setText("Per favore, completa tutti i campi prima di continuare.");
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
        loginErrorMessage.setText("Il nickname che hai scelto è già stato preso");
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
        createLobbyButton.setFont(new Font(LOGIN_FONT, 14.0));
        createLobbyButton.setText("Crea una partita");
        createLobbyButton.setTextAlignment(TextAlignment.CENTER);
        joinLobbyButton.setLayoutX(20.0);
        joinLobbyButton.setLayoutY(350.0);
        joinLobbyButton.setMnemonicParsing(false);
        joinLobbyButton.setPrefWidth(281.0);
        joinLobbyButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        joinLobbyButton.setFont(new Font(LOGIN_FONT, 14.0));
        joinLobbyButton.setText("Entra in una partita");
        joinLobbyButton.setTextAlignment(TextAlignment.CENTER);

        createLobbyButton.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 0)));
        joinLobbyButton.setOnAction(actionEvent -> client.asyncWriteToSocket(new CommunicationMessage(CommunicationMessage.MessageType.JOINING_ACTION_INFO, 1)));

        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(createLobbyButton);
        contentPane.getChildren().add(joinLobbyButton);
    }

    public void displayNoLobbiesAvailable() {
        Logger.INFO("No lobbies are available. Please chose another option.");
        loginErrorMessage.setText("Non ci sono lobby aperte al momento. Per favore, scegli un'altra opzione.");
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/noLobbiesAvailableError.png"))));
        errorBox.setVisible(true);
    }

    public void askNumberOfPlayerView(){
        Logger.INFO("Asking number of players");
        errorBox.setVisible(false);
        contentPane.getChildren().clear();
        ImageView backgroundImg = new ImageView();
        Button twoPlayersButton = new Button();
        Button threePlayersButton = new Button();
        Button fourPlayersButton = new Button();
        HBox switchGameModeBox = new HBox();
        CustomSwitch customGameModeSwitch = new CustomSwitch();
        ImageView switchImg = new ImageView();
        Text gameModeTxt = new Text();

        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoSorcerer.png"))));
        backgroundImg.setFitHeight(749.0);
        backgroundImg.setFitWidth(521.0);
        backgroundImg.setLayoutX(-37.0);
        backgroundImg.setLayoutY(13.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setPickOnBounds(true);

        twoPlayersButton.setLayoutX(17.0);
        twoPlayersButton.setLayoutY(391.0);
        twoPlayersButton.setMnemonicParsing(false);
        twoPlayersButton.setPrefWidth(88.0);
        twoPlayersButton.setPrefHeight(37.0);
        twoPlayersButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        twoPlayersButton.setFont(new Font(LOGIN_FONT, 14.0));
        twoPlayersButton.setText("2");
        twoPlayersButton.setTextAlignment(TextAlignment.CENTER);

        threePlayersButton.setLayoutX(120.0);
        threePlayersButton.setLayoutY(391.0);
        threePlayersButton.setMnemonicParsing(false);
        threePlayersButton.setPrefWidth(88.0);
        threePlayersButton.setPrefHeight(37.0);
        threePlayersButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        threePlayersButton.setFont(new Font(LOGIN_FONT, 14.0));
        threePlayersButton.setText("3");
        threePlayersButton.setTextAlignment(TextAlignment.CENTER);

        fourPlayersButton.setLayoutX(219.0);
        fourPlayersButton.setLayoutY(391.0);
        fourPlayersButton.setMnemonicParsing(false);
        fourPlayersButton.setPrefWidth(88.0);
        fourPlayersButton.setPrefHeight(37.0);
        fourPlayersButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
        fourPlayersButton.setFont(new Font(LOGIN_FONT, 14.0));
        fourPlayersButton.setText("4");
        fourPlayersButton.setTextAlignment(TextAlignment.CENTER);

        twoPlayersButton.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 2)));
        threePlayersButton.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 3)));
        fourPlayersButton.setOnAction(ActionEvent -> client.asyncWriteToSocket(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, 4)));

        switchGameModeBox.setAlignment(Pos.CENTER);
        switchGameModeBox.setLayoutX(14.0);
        switchGameModeBox.setLayoutY(326.0);
        switchGameModeBox.setSpacing(10);
        switchImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/normalGame.png"))));
        switchImg.setFitHeight(51.0);
        switchImg.setFitWidth(54.0);
        switchImg.setPickOnBounds(true);
        switchImg.setPreserveRatio(true);
        gameModeTxt.setStrokeType(StrokeType.OUTSIDE);
        gameModeTxt.setStrokeWidth(0.0);
        gameModeTxt.setWrappingWidth(113.13671875);
        gameModeTxt.setText(NORMAL_GAME_TXT);
        gameModeTxt.setFill(Color.rgb(225,225,225));
        gameModeTxt.setFont(new Font(LOGIN_FONT, 14));
        switchGameModeBox.getChildren().add(switchImg);
        switchGameModeBox.getChildren().add(gameModeTxt);
        switchGameModeBox.getChildren().add(customGameModeSwitch);
        customGameModeSwitch.setOnMouseClicked(event -> {
            customGameModeSwitch.changeState();
            changeStateCallback(switchImg, gameModeTxt, customGameModeSwitch.getState());
        });

        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(twoPlayersButton);
        contentPane.getChildren().add(threePlayersButton);
        contentPane.getChildren().add(fourPlayersButton);
        contentPane.getChildren().add(switchGameModeBox);
    }

    private void changeStateCallback(ImageView img, Text txt, boolean switchState) {
        if(switchState) {
            img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/expertGame.png"))));
            txt.setText(EXPERT_GAME_TXT);
            isExpertGame = true;
        } else {
            img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/normalGame.png"))));
            txt.setText(NORMAL_GAME_TXT);
            isExpertGame = false;
        }
    }

    public void askGameTypeView(){
        Logger.INFO("Asking game type");
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



    public void setMessageHandler(ClientMessageObserverHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
