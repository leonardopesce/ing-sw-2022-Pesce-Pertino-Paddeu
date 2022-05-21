package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.controller.custom_gui.CustomSwitch;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
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
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class LoginController implements Initializable {
    private static final String LOGIN_FONT = "Berlin Sans FB";
    private static final String NORMAL_GAME_TXT = "Regole base";
    private static final String EXPERT_GAME_TXT = "Regole per esperti";
    private Client client;
    private ClientMessageObserverHandler messageHandler;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isExpertGame = false;
    private Optional<DeckType> deckTypeChosen = Optional.empty();

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
                        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/connectionError.gif"))));
                        errorBox.setVisible(true);
                        client = null;
                    }
                }).start();
            } else {
                errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/invalidAccessInfo.gif"))));
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
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/login/nicknameAlreadyTakenError.gif"))));
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
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/noLobbiesAvailableError.gif"))));
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
        client.asyncWriteToSocket(new CommunicationMessage(GAME_TYPE_INFO, isExpertGame));
    }

    public void displayLobbyJoined(LobbyInfo lobbyInfos) {
        Logger.INFO("Succesfully joined a lobby.");
        errorBox.setVisible(false);
        contentPane.getChildren().clear();
        ListView<HBox> lobbyMembers = new ListView<>();
        ImageView backgroundImg = new ImageView();
        Text lobbyText = new Text("Giocatori" + " ~ " + lobbyInfos.getCurrentLobbySize() + "/" + lobbyInfos.getLobbyMaxSize());

        backgroundImg.setFitHeight(633.0);
        backgroundImg.setFitWidth(438.0);
        backgroundImg.setLayoutX(-20.0);
        backgroundImg.setLayoutY(9.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPickOnBounds(true);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoWizard.png"))));

        lobbyText.setFill(Color.rgb(225,225,225));
        lobbyText.setLayoutY(222.0);
        lobbyText.setStrokeType(StrokeType.OUTSIDE);
        lobbyText.setStrokeWidth(0.0);
        lobbyText.setWrappingWidth(321.0);
        lobbyText.setTextAlignment(TextAlignment.CENTER);
        lobbyText.setFont(new Font(LOGIN_FONT, 24.0));

        lobbyMembers.setStyle("-fx-background-color: transparent");
        lobbyMembers.setLayoutX(9.0);
        lobbyMembers.setLayoutY(232.0);
        lobbyMembers.setPrefHeight(223.0);
        lobbyMembers.setPrefWidth(303.0);
        List<HBox> lobbyMembersObjects = populateLobbyMembersObjectsArray(lobbyInfos);
        lobbyMembers.getItems().addAll(lobbyMembersObjects);

        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(lobbyText);
        contentPane.getChildren().add(lobbyMembers);
    }

    private List<HBox> populateLobbyMembersObjectsArray(LobbyInfo lobbyInfo) {
        List<HBox> compiledLobbyMembersList = new ArrayList<>();

        for(String lobbyMember : lobbyInfo.getLobbyMembers()) {
            HBox memberBox = new HBox();
            ImageView ownerIcon = new ImageView();
            Text memberText = new Text(lobbyMember);
            memberBox.setAlignment(Pos.CENTER_LEFT);
            memberBox.setPrefHeight(37.0);
            memberBox.setPrefWidth(292.0);
            ownerIcon.setFitWidth(35.0);
            ownerIcon.setFitHeight(37.0);
            ownerIcon.setPickOnBounds(true);
            ownerIcon.setPreserveRatio(true);
            if(lobbyMember.equals(lobbyInfo.getLobbyName())) {
                ownerIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/lobbyOwner.gif"))));
            }
            memberText.setStrokeType(StrokeType.OUTSIDE);
            memberText.setStrokeWidth(0.0);
            memberText.setFont(new Font(LOGIN_FONT, 18.0));
            memberText.setFill(Color.rgb(225,225,225));

            memberBox.getChildren().add(ownerIcon);
            memberBox.getChildren().add(memberText);
            memberBox.setSpacing(10.0);
            compiledLobbyMembersList.add(memberBox);
        }

        return compiledLobbyMembersList;
    }

    public void askLobbyToJoinView(Object listOfLobbyInfos){
        Logger.INFO("Asking lobby to join");
        errorBox.setVisible(false);
        contentPane.getChildren().clear();
        ListView<HBox> listOfLobbies = new ListView<>();
        ImageView backgroundImg = new ImageView();
        HBox listViewHeader = new HBox();
        Text listViewHeaderTitle = new Text("Lista di lobby");
        Separator listViewHeaderVerticalSeparator = new Separator();
        Button listViewHeaderGoBackButton = new Button();
        ImageView listViewHeaderGoBackButtonIcon = new ImageView();
        List<HBox> availableLobbies = populateListOfLobbiesArrayObject((List<LobbyInfo>) listOfLobbyInfos);

        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoSorcerer.png"))));
        backgroundImg.setFitHeight(749.0);
        backgroundImg.setFitWidth(521.0);
        backgroundImg.setLayoutX(-37.0);
        backgroundImg.setLayoutY(13.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setPickOnBounds(true);
        backgroundImg.setMouseTransparent(true);

        listViewHeader.setAlignment(Pos.CENTER_LEFT);
        listViewHeader.setLayoutX(4.0);
        listViewHeader.setLayoutY(183.0);
        listViewHeader.setPrefWidth(303.0);
        listViewHeader.setPrefHeight(32.0);
        listViewHeaderTitle.setStrokeType(StrokeType.OUTSIDE);
        listViewHeaderTitle.setFill(Color.rgb(225,225,225));
        listViewHeaderTitle.setStrokeWidth(0.0);
        listViewHeaderTitle.setTextAlignment(TextAlignment.CENTER);
        listViewHeaderTitle.setWrappingWidth(155.0);
        listViewHeaderTitle.setFont(new Font(LOGIN_FONT, 24.0));
        listViewHeaderVerticalSeparator.setOrientation(Orientation.VERTICAL);
        listViewHeaderVerticalSeparator.setPrefHeight(32.0);
        listViewHeaderVerticalSeparator.setPrefWidth(127.0);
        listViewHeaderVerticalSeparator.setVisible(false);
        listViewHeaderGoBackButton.setMnemonicParsing(false);
        listViewHeaderGoBackButton.setPrefHeight(32.0);
        listViewHeaderGoBackButton.setPrefWidth(28.0);
        listViewHeaderGoBackButton.setStyle("-fx-background-color: transparent");
        listViewHeaderGoBackButtonIcon.setFitHeight(22.0);
        listViewHeaderGoBackButtonIcon.setFitWidth(45.0);
        listViewHeaderGoBackButtonIcon.setPickOnBounds(true);
        listViewHeaderGoBackButtonIcon.setPreserveRatio(true);
        listViewHeaderGoBackButtonIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/goBackButton.png"))));
        listViewHeaderGoBackButton.setGraphic(listViewHeaderGoBackButtonIcon);
        listViewHeaderGoBackButton.setOnAction(event -> goBackCallback());
        listViewHeader.getChildren().add(listViewHeaderTitle);
        listViewHeader.getChildren().add(listViewHeaderVerticalSeparator);
        listViewHeader.getChildren().add(listViewHeaderGoBackButton);

        listOfLobbies.setLayoutX(9.0);
        listOfLobbies.setLayoutY(232.0);
        listOfLobbies.setPrefWidth(303.0);
        listOfLobbies.setPrefHeight(223.0);
        listOfLobbies.setStyle("-fx-background-color: transparent;");
        listOfLobbies.getItems().addAll(availableLobbies);
        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(listViewHeader);
        contentPane.getChildren().add(listOfLobbies);
    }

    private void goBackCallback() {
        Logger.INFO("Going back to joining action.");
        client.asyncWriteToSocket(new CommunicationMessage(LOBBY_TO_JOIN_INFO, "GO_BACK_TO_JOIN_ACTION"));
    }

    private List<HBox> populateListOfLobbiesArrayObject(List<LobbyInfo> lobbiesInfos) {
        List<HBox> compiledLobbiesInfos = new ArrayList<>();

        for(LobbyInfo lobby : lobbiesInfos) {
            HBox lobbyObject = new HBox();
            ImageView gameTypeIcon = new ImageView();
            HBox lobbyObjectContent = new HBox();
            Text lobbyName = new Text("Lobby di " + lobby.getLobbyName());
            Text lobbySize = new Text();
            Button accessLobbyButton = new Button();

            lobbyObject.setAlignment(Pos.CENTER_LEFT);
            lobbyObject.setSpacing(5.0);
            lobbyObject.setMinWidth(0.0);
            lobbyObject.setPrefHeight(37.0);
            lobbyObject.setPrefWidth(289.0);
            gameTypeIcon.setFitHeight(45.0);
            gameTypeIcon.setFitWidth(45.0);
            gameTypeIcon.setPickOnBounds(true);
            gameTypeIcon.setPreserveRatio(true);
            if(lobby.isLobbyExpert()) {
                gameTypeIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/expertGame.png"))));
            } else {
                gameTypeIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/normalGame.png"))));
            }
            lobbyObjectContent.setAlignment(Pos.CENTER_LEFT);
            lobbyObjectContent.setSpacing(7.0);
            lobbyObjectContent.setMinWidth(0.0);
            lobbyObjectContent.setPrefHeight(37.0);
            lobbyObjectContent.setPrefWidth(179.0);
            lobbyName.setStrokeType(StrokeType.OUTSIDE);
            lobbyName.setStrokeWidth(0.0);
            lobbyName.setFont(new Font(LOGIN_FONT, 18.0));
            lobbyName.setFill(Color.rgb(225,225,225));
            lobbySize.setStrokeType(StrokeType.OUTSIDE);
            lobbySize.setStrokeWidth(0.0);
            lobbySize.setFont(new Font(LOGIN_FONT, 22.0));
            lobbySize.setText(lobby.getCurrentLobbySize() + "/" + lobby.getLobbyMaxSize());
            if(lobby.isFull()) {
                lobbySize.setFill(Color.rgb(154,54,91));
                accessLobbyButton.setDisable(true);
                accessLobbyButton.setVisible(false);
            } else {
                lobbySize.setFill(Color.rgb(7, 94, 84));
                accessLobbyButton.setDisable(false);
                accessLobbyButton.setVisible(true);
            }
            lobbyObjectContent.getChildren().add(lobbyName);
            lobbyObjectContent.getChildren().add(lobbySize);
            accessLobbyButton.setMnemonicParsing(false);
            accessLobbyButton.setStyle("-fx-background-color: #9a365b; -fx-background-radius: 50px; -fx-text-fill: #e1e1e1;");
            accessLobbyButton.setText("Accedi");
            accessLobbyButton.setOnAction(event -> client.asyncWriteToSocket(new CommunicationMessage(LOBBY_TO_JOIN_INFO, lobby.getLobbyName())));

            lobbyObject.getChildren().add(gameTypeIcon);
            lobbyObject.getChildren().add(lobbyObjectContent);
            lobbyObject.getChildren().add(accessLobbyButton);

            compiledLobbiesInfos.add(lobbyObject);
        }

        return compiledLobbiesInfos;
    }

    public void displayOtherPlayerIsChoosingHisDeckType(String otherPlayerName) {
        contentPane.getChildren().clear();
        ImageView backgroundImg = new ImageView();

        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoMotherNature.png"))));
        backgroundImg.setFitHeight(633.0);
        backgroundImg.setFitWidth(438.0);
        backgroundImg.setLayoutX(-53.0);
        backgroundImg.setLayoutY(-54.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setPickOnBounds(true);
        backgroundImg.setMouseTransparent(true);

        contentPane.getChildren().add(backgroundImg);

        if(deckTypeChosen.isPresent()) {
            ImageView deckTypeChosenImage = new ImageView();
            Text deckTypeName = new Text(this.deckTypeChosen.get().getName());
            Text deckTypeSignatureText = new Text(switch (deckTypeChosen.get()) {
                case KING -> "\"Che ora è?\"\n\"Sì.\"";
                case PIXIE -> "\"Facciamo un esempietto\"";
                case ELDER -> "\"Siccome che mi hai scelto, vinceremo questa partita!\"";
                case SORCERER -> "\"Se vinciamo la partita, potrò permettermi il Colosseo della Lego.\"";
            });
            deckTypeChosenImage.setFitHeight(204.0);
            deckTypeChosenImage.setFitWidth(167.0);
            deckTypeChosenImage.setLayoutX(88.0);
            deckTypeChosenImage.setLayoutY(140.0);
            deckTypeChosenImage.setPickOnBounds(true);
            deckTypeChosenImage.setPreserveRatio(true);
            deckTypeChosenImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(switch (deckTypeChosen.get()) {
                case KING -> "/img/login/launcherSplashLogo.png";
                case PIXIE -> "/img/menu/launcherSplashLogoPixie.png";
                case SORCERER -> "/img/menu/launcherSplashLogoWizard.png";
                case ELDER -> "/img/menu/launcherSplashLogoSorcerer.png";
            }))));
            deckTypeName.setFill(Color.rgb(154,54,91));
            deckTypeName.setLayoutY(368.0);
            deckTypeName.setStrokeType(StrokeType.OUTSIDE);
            deckTypeName.setStrokeWidth(0.0);
            deckTypeName.setWrappingWidth(321.0);
            deckTypeName.setTextAlignment(TextAlignment.CENTER);
            deckTypeName.setFont(new Font(LOGIN_FONT, 36.0));
            deckTypeSignatureText.setFill(Color.rgb(225,225,225));
            deckTypeSignatureText.setTextAlignment(TextAlignment.CENTER);
            deckTypeSignatureText.setWrappingWidth(321.0);
            deckTypeSignatureText.setLayoutY(394.0);
            deckTypeSignatureText.setStrokeType(StrokeType.OUTSIDE);
            deckTypeSignatureText.setStrokeWidth(0.0);
            deckTypeSignatureText.setFont(new Font(LOGIN_FONT, 21.0));

            contentPane.getChildren().add(deckTypeChosenImage);
            contentPane.getChildren().add(deckTypeName);
            contentPane.getChildren().add(deckTypeSignatureText);
        }

        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/loading.gif"))));
        loginErrorMessage.setText("La partita è iniziata e " + otherPlayerName + " sta scelgiendo il suo personaggio. Tra pochi istanti sarà il tuo turno.");
        errorBox.setVisible(true);
    }

    public void askDeckView(List<DeckType> listAvailableDeck){
        Logger.INFO("Asking deck");
        errorBox.setVisible(false);
        contentPane.getChildren().clear();
        ImageView backgroundImg = new ImageView();
        GridPane cardGridPane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        ImageView kingRetro = new ImageView();
        ImageView pixieRetro = new ImageView();
        ImageView sorcererRetro = new ImageView();
        ImageView wizardRetro = new ImageView();


        column1.setHgrow(Priority.SOMETIMES);
        column1.setMinWidth(10.0);
        column1.setPrefWidth(100.0);
        column2.setHgrow(Priority.SOMETIMES);
        column2.setMinWidth(10.0);
        row1.setMinHeight(10.0);
        row1.setVgrow(Priority.SOMETIMES);
        row2.setMinHeight(10.0);
        row2.setVgrow(Priority.SOMETIMES);
        column2.setPrefWidth(100.0);
        cardGridPane.setHgap(20.0);
        cardGridPane.setVgap(20.0);
        cardGridPane.setLayoutX(58.0);
        cardGridPane.setLayoutY(133.0);
        cardGridPane.setPrefWidth(200.0);
        cardGridPane.getColumnConstraints().add(column1);
        cardGridPane.getColumnConstraints().add(column2);
        cardGridPane.getRowConstraints().add(row1);
        cardGridPane.getRowConstraints().add(row2);
        kingRetro.setFitHeight(150.0);
        kingRetro.setFitWidth(200.0);
        kingRetro.setPickOnBounds(true);
        kingRetro.setPreserveRatio(true);
        kingRetro.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/kingRetroClipped.png"))));
        if(!listAvailableDeck.contains(DeckType.KING)) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.5);
            GaussianBlur imageBlur = new GaussianBlur();
            colorAdjust.setInput(imageBlur);
            kingRetro.setEffect(colorAdjust);
        } else {
            kingRetro.setOnMouseClicked(click -> {
                client.asyncWriteToSocket(new CommunicationMessage(DECK_TYPE_MESSAGE, DeckType.KING));
                deckTypeChosen = Optional.of(DeckType.KING);
            });
        }
        cardGridPane.add(kingRetro, 0, 0);
        pixieRetro.setFitHeight(150.0);
        pixieRetro.setFitWidth(200.0);
        pixieRetro.setPickOnBounds(true);
        pixieRetro.setPreserveRatio(true);
        pixieRetro.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/pixieRetroClipped.png"))));
        if(!listAvailableDeck.contains(DeckType.PIXIE)) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.5);
            GaussianBlur imageBlur = new GaussianBlur();
            colorAdjust.setInput(imageBlur);
            pixieRetro.setEffect(colorAdjust);
        } else {
            pixieRetro.setOnMouseClicked(click -> {
                client.asyncWriteToSocket(new CommunicationMessage(DECK_TYPE_MESSAGE, DeckType.PIXIE));
                deckTypeChosen = Optional.of(DeckType.PIXIE);
            });
        }
        cardGridPane.add(pixieRetro, 0, 1);
        sorcererRetro.setFitHeight(150.0);
        sorcererRetro.setFitWidth(200.0);
        sorcererRetro.setPickOnBounds(true);
        sorcererRetro.setPreserveRatio(true);
        sorcererRetro.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/sorcererRetroClipped.png"))));
        if(!listAvailableDeck.contains(DeckType.ELDER)) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.5);
            GaussianBlur imageBlur = new GaussianBlur();
            colorAdjust.setInput(imageBlur);
            sorcererRetro.setEffect(colorAdjust);
        } else {
            sorcererRetro.setOnMouseClicked(click -> {
                client.asyncWriteToSocket(new CommunicationMessage(DECK_TYPE_MESSAGE, DeckType.ELDER));
                deckTypeChosen = Optional.of(DeckType.ELDER);
            });
        }
        cardGridPane.add(sorcererRetro, 1, 0);
        wizardRetro.setFitHeight(150.0);
        wizardRetro.setFitWidth(200.0);
        wizardRetro.setPickOnBounds(true);
        wizardRetro.setPreserveRatio(true);
        wizardRetro.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/wizardRetroClipped.png"))));
        if(!listAvailableDeck.contains(DeckType.SORCERER)) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.5);
            GaussianBlur imageBlur = new GaussianBlur();
            colorAdjust.setInput(imageBlur);
            wizardRetro.setEffect(colorAdjust);
        } else {
            wizardRetro.setOnMouseClicked(click -> {
                client.asyncWriteToSocket(new CommunicationMessage(DECK_TYPE_MESSAGE, DeckType.SORCERER));
                deckTypeChosen = Optional.of(DeckType.SORCERER);
            });
        }
        cardGridPane.add(wizardRetro, 1, 1);

        backgroundImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/launcherSplashLogoMotherNature.png"))));
        backgroundImg.setFitHeight(633.0);
        backgroundImg.setFitWidth(438.0);
        backgroundImg.setLayoutX(-53.0);
        backgroundImg.setLayoutY(-54.0);
        backgroundImg.setOpacity(0.19);
        backgroundImg.setPreserveRatio(true);
        backgroundImg.setPickOnBounds(true);
        backgroundImg.setMouseTransparent(true);

        contentPane.getChildren().add(backgroundImg);
        contentPane.getChildren().add(cardGridPane);
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/yourTurn.gif"))));
        loginErrorMessage.setText("É il tuo turno! Scegli il tuo mago.");
        errorBox.setVisible(true);
    }

    public void setOnDisconnection(String disconnectedPlayer) {
        Logger.ERROR(disconnectedPlayer + "'s connection has been interrupted. The lobby will now close and you will be disconnected from the server.", "Player disconnection");
        errorLogo.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/disconnection.gif"))));
        loginErrorMessage.setText(disconnectedPlayer +  " si è disconnesso. Sei stato rimosso dalla lobby e disconnesso dal server.");
        errorBox.setVisible(true);
    }

    public Client getClient() {
        return client;
    }

    public void setMessageHandler(ClientMessageObserverHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}
