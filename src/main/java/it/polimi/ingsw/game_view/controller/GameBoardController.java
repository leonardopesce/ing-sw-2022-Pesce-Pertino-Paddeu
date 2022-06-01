package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_view.board.*;
import it.polimi.ingsw.network.client.Client;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;
import static it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;

public class GameBoardController implements Initializable {
    private Client client;
    private String clientName;
    private boolean firstTime = true;
    private final boolean[] isUp = {false, false, false, false, false, false, false, false, false, false};
    private final IntegerProperty showedBoard = new SimpleIntegerProperty(0);
    final RotateTransition rotateTransition = new RotateTransition();
    private final List<Button> playerBoardButtons = new ArrayList<>();
    private final List<ImageView> assistants = new ArrayList<>();
    private final List<IslandController> islands = new ArrayList<>();
    private int playingAdvancedCard = -1;
    private GameBoard gameBoard;
    private final Stack<Integer> actionValues = new Stack<>();
    private final List<CloudController> clouds = new ArrayList<>();
    private final List<AdvancedCardController> advancedCards = new ArrayList<>();
    private final Image infoLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/infoLogo.gif")));
    private final Image errorLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/errorLogo.gif")));
    private final Image notYourTurnLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/loading.gif")));
    private final Image yourTurnLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/menu/yourTurn.gif")));
    private final Image notEnoughMoneyLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/money.gif")));
    private final Image winLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/winLogo.gif")));
    private final Image loseLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/loseLogo.gif")));
    private final Image drawLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/drawLogo.gif")));
    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10, winAnimation, loseAnimation, drawAnimation, commentLogo;
    @FXML
    private Button player1Board, player2Board, player3Board, player4Board;
    @FXML
    private Label gamePhaseLabel, comment;
    @FXML
    private StackPane mainPane;
    @FXML
    private HBox cards, cloudHBox, commentBox;
    @FXML
    private VBox advancedBoard;
    @FXML
    private IslandController island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller;
    @FXML
    private RotatingBoardController rotatingBoardController;
    @FXML
    private CloudController cloud0Controller, cloud1Controller, cloud2Controller, cloud3Controller;
    @FXML
    private AdvancedCardController advancedCard0Controller, advancedCard1Controller, advancedCard2Controller;

    /**
     * Initialize function, initializes all the variable used by the class
     * @param url handle by javafx during loading of fxml
     * @param resourceBundle handle by javafx during loading of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // init used arrays
        advancedCards.addAll(Arrays.asList(advancedCard0Controller, advancedCard1Controller, advancedCard2Controller));
        clouds.addAll(Arrays.asList(cloud0Controller, cloud1Controller, cloud2Controller, cloud3Controller));
        playerBoardButtons.addAll(Arrays.asList(player1Board, player2Board, player3Board, player4Board));
        islands.addAll(Arrays.asList(island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller));
        assistants.addAll(Arrays.asList(assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10));

        //setup object in main board
        setCommentBoxNotVisible();
        mainPane.setBackground(new Background(new BackgroundImage(new Image("img/table.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));

        //set up the id of each island
        for(int i = 0; i < islands.size(); i++){
            islands.get(i).setID(i);
        }

        // setup rotate animation
        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setCycleCount(1);
        rotateTransition.setDuration(Duration.millis(1500));
        rotateTransition.setAutoReverse(false);
        rotateTransition.setNode(rotatingBoardController.getPane());

    }

    /**
     * Setter for the client instance, used to send a message when the player makes a move
     * @param client to be set
     * @see Client
     */
    public void setClient(Client client) {
        this.client = client;
        clientName = client.getName();
        showedBoard.addListener((observable, oldValue, newValue) -> new Thread(() -> setUpDecks((Integer) newValue)).start());
    }

    /**
     * Setter for the client name used to make the client work when in testing mode
     * @param name the name of the player
     */
    public void setClientName(String name){
        //TODO delete, just for testing purpose
        this.clientName = name;
        showedBoard.addListener((observable, oldValue, newValue) -> new Thread(() -> setUpDecks((Integer) newValue)).start());
    }

    /**
     * Function that based on the value saved in the actionValues stack (and on the value of playingAdvancedCard)
     * determines which command to send to the server
     */
    protected void calculateNextAction(){
        if(playingAdvancedCard == -1) {
            //If the action is a normal action send to the server the action requested based on the gamePhase board
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION,
                    switch (gameBoard.getPhase()) {
                        case PLANNING_PHASE -> new PlayAssistantCardAction(clientName, actionValues.pop());
                        case ACTION_PHASE_MOVING_STUDENTS -> actionValues.get(0) == gameBoard.getTerrain().getIslands().size() ?
                                new MoveStudentToDiningHallAction(clientName, actionValues.pop()) :
                                new MoveStudentToIslandAction(clientName, actionValues.pop(), actionValues.pop());
                        case ACTION_PHASE_MOVING_MOTHER_NATURE -> new MoveMotherNatureAction(clientName, countStepFromMotherNatureToIslandWithID(actionValues.pop()));
                        case ACTION_PHASE_CHOOSING_CLOUD -> new ChooseCloudCardAction(clientName, actionValues.pop());
                        default -> throw new IllegalStateException("Unexpected value: " + gameBoard.getPhase());
                    }));
        }
        else if(playingAdvancedCard == 1){
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], actionValues.toArray())));
        }
        else if (playingAdvancedCard == MERCHANT.ordinal() || playingAdvancedCard == LANDLORD.ordinal()) {
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], ColorCharacter.values()[actionValues.pop()])));
        }
        else if(playingAdvancedCard == POSTMAN.ordinal()) {
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], clientName)));
        }
        else if(playingAdvancedCard == PRINCESS.ordinal()){
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], clientName, actionValues.pop())));
        }
        else if(playingAdvancedCard == BARD.ordinal()){
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], clientName,
                    actionValues.size() == 2 ? Arrays.asList(actionValues.pop()) : Arrays.asList(actionValues.pop(), actionValues.pop()),
                    actionValues.size() == 1 ? Arrays.asList(ColorCharacter.values()[actionValues.pop()]) : Arrays.asList(ColorCharacter.values()[actionValues.pop()], ColorCharacter.values()[actionValues.pop()]))));
        }
        else if(playingAdvancedCard == JESTER.ordinal()){
            System.out.println(actionValues);
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(clientName, AdvancedCharacterType.values()[actionValues.pop()], clientName,
                    actionValues.size() == 2 ? Arrays.asList(actionValues.pop()) : actionValues.size() == 4 ? Arrays.asList(actionValues.pop(), actionValues.pop()) : Arrays.asList(actionValues.pop(), actionValues.pop(), actionValues.pop()),
                    actionValues.size() == 1 ? Arrays.asList(actionValues.pop()) : actionValues.size() == 2 ? Arrays.asList(actionValues.pop(), actionValues.pop()) : Arrays.asList(actionValues.pop(), actionValues.pop(), actionValues.pop()))));
        }
        actionValues.clear();
    }

    /**
     * Calls the update of the entire board in the game every time a new message is received from the server
     * @param board a GameBoard containing the new state of the game send by the server
     */
    public void updateBoard(GameBoard board){
        Platform.runLater(() -> {
            setCommentBoxNotVisible();
            actionValues.clear();
            gameBoard = board;
            playingAdvancedCard = -1;
            rotatingBoardController.update(board);
            gamePhaseLabel.setText(board.getPhase().getName());

            firstTimeSetupBoard(board);

            setUpPlayersCards(board);

            setUpIslands(board);

            for(int i = 0; i < board.getTerrain().getCloudCards().size(); i++){
                clouds.get(i).update(board.getTerrain().getCloudCards().get(i));
            }

            setUpCommentBox(board);

            setUpExpertMode(board);
        });
    }

    /**
     * A setup of the board needed only for the first update received from the server
     * @param board update from the server
     */
    private void firstTimeSetupBoard(GameBoard board){
        if (firstTime) {
            firstTime = false;
            for (int i = 0; i < 4; i++) {
                if (i < board.getNames().size()) {
                    //sets players buttons
                    playerBoardButtons.get(i).setText(board.getNames().get(i));
                    int finalI = i;
                    playerBoardButtons.get(i).setOnAction(ActionEvent -> {
                        synchronized (rotateTransition) {
                            setRotatingButtonDisabled(true);
                            rotateTransition.setByAngle(getDegreeTurn(finalI));
                            showedBoard.set(finalI);
                            rotateTransition.setOnFinished(a -> {
                                setRotatingButtonDisabled(false);
                                playerBoardButtons.get(finalI).setDisable(true);
                                playerBoardButtons.get(finalI).setDisable(true);
                            });
                            Platform.runLater(rotateTransition::play);
                        }
                    });
                } else { //hides the player's boards and buttons not needed
                    rotatingBoardController.getBoardX(i).hide();
                    playerBoardButtons.get(i).setDisable(true);
                    playerBoardButtons.get(i).setVisible(false);
                }
            }
            setUpDecks(showedBoard.get());
            // removes the cloud not needed
            for(int i = 4; i > board.getTerrain().getCloudCards().size(); i--){
                clouds.remove(i - 1);
                cloudHBox.getChildren().remove(i - 1);
            }

        }
    }

    /**
     * Sets up the card based on the update received by the server, obscures the already used ones
     * @param board update from the server
     */
    private void setUpPlayersCards(GameBoard board){
        for (int i = 0; i < board.getNames().size(); i++) {
            if (playerBoardButtons.get(i).getText().equals(clientName)) {
                ColorAdjust ca = new ColorAdjust();
                for(int k = 0; k < assistants.size(); k++){
                    if(!rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(a -> a.getType().getCardTurnValue()).toList().contains(k + 1)) {
                        ca.setBrightness(-0.5);
                        assistants.get(k).setEffect(ca);
                    }
                }
                playerBoardButtons.get(i).fire();
            }
        }
    }

    /**
     * Sets up the islands based on the update received from the server hides discarded islands and calls updates on the other ones
     * @param board update from the server
     */
    private void setUpIslands(GameBoard board){
        for (IslandController island: islands) {
            if(board.getTerrain().getIslands().stream().map(IslandBoard::getID).toList().contains(island.getID())){
                island.unHide();
                island.update(board.getTerrain().getIslandWithID(island.getID()));
            }
            else{
                island.hide();
            }
        }
    }

    /**
     * Sets up the comment section of the screen based on the current game phase
     * @param board update from the server
     */
    private void setUpCommentBox(GameBoard board){
        if (board.getCurrentlyPlaying().equals(clientName)) {
            setCommentLogo(yourTurnLogo);
            if (board.getPhase().equals(GamePhase.PLANNING_PHASE)) {
                setComment("E' il tuo turno! Gioca una carta assistente.");
                makeAssistantCardPlayable();
            }
            else if (board.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_STUDENTS)) {
                setComment("E' il tuo turno! Muovi gli studenti.");
                makeStudentEntranceSelectable();
            }
            else if(board.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_MOTHER_NATURE)){
                setComment("Ora scegli dove muovere madre natura. Puoi spostarla di un massimo di " + board.getDecks().get(board.getNames().indexOf(clientName)).getDiscardedCard().getMaximumSteps() + " passi dalla sua posizione attuale.");
                makeNextXIslandVisibleFromMotherNatureSelectable(board.getDecks().get(board.getNames().indexOf(clientName)).getDiscardedCard().getMaximumSteps());
            }
            else if(board.getPhase().equals(GamePhase.ACTION_PHASE_CHOOSING_CLOUD)){
                setComment("Scegli infine una carta nuvola per raccogliere gli studenti che vi sono sopra.");
                makeCloudSelectable();
            }
            setCommentBoxVisible();
        } else {
            gamePhaseLabel.setText("Non è il tuo turno");
            setComment("E' il turno di " + board.getCurrentlyPlaying() + ". Tra poco toccherà a te...");
            setCommentLogo(notYourTurnLogo);
            setCommentBoxVisible();
        }
    }

    /**
     * sets up the board in the case the game played is in expert mode: shows, updates and makes selectable the advanced cards
     * @param board update from the server
     */
    private void setUpExpertMode(GameBoard board){
        if(gameBoard.isExpertMode()){
            advancedBoard.setVisible(true);
            for(int i = 0; i < advancedCards.size(); i++){
                advancedCards.get(i).update(gameBoard.getTerrain().getAdvancedCard().get(i));
            }
            if (board.getPhase() != GamePhase.PLANNING_PHASE && !gameBoard.isHasPlayedSpecialCard()) {
                makeAdvancedCardSelectable();
            }
            else{
                for (AdvancedCardController advancedCard : advancedCards) {
                    advancedCard.getCardImage().setEffect(null);
                    advancedCard.getCardImage().setOnMouseEntered(null);
                    advancedCard.getCardImage().setOnMouseExited(null);
                    advancedCard.getCardImage().setOnMouseClicked(null);
                }
            }
        }
        else{
            advancedBoard.setVisible(false);
        }
    }

    /**
     * Shows the animation for the end of the game
     * @param condition end game condition (win, lose or draw)
     */
    public void makeEndAnimation(CommunicationMessage.MessageType condition){
        ImageView toShow;

        if(condition.equals(YOU_WIN)) {
            toShow = winAnimation;
            setComment("Hai vinto la partita!");
            setCommentLogo(winLogo);
        } else if(condition.equals(YOU_LOSE)) {
            toShow = loseAnimation;
            setComment("Hai perso la partita.");
            setCommentLogo(loseLogo);
        } else {
            toShow = drawAnimation;
            setComment("La partita è terminata in parità.");
            setCommentLogo(drawLogo);
        }

        setCommentBoxVisible();
        toShow.setVisible(true);
    }

    /**
     * sets up the assistant card showing the back of the card if the board is not the one of the player
     * @param pos index of the board related to the deck
     */
    private void setUpDecks(int pos){
        if(rotatingBoardController.getBoardX(pos).getName().getText().equals(clientName)){
            setAssistantsCardsFront();
        }
        else {
            setAssistantsCardsRetro(new Image(rotatingBoardController.getBoardX(pos).getDeckBoard().getDeckType().getPath()));
        }
    }

    /**
     * Set up the back of the card when the player is not on his own school
     * @param image the image to set in the retro of the card
     */
    private void setAssistantsCardsRetro(Image image){
        ColorAdjust ca = new ColorAdjust();
        for(int i = 0; i < assistants.size(); i++){
            assistants.get(i).setImage(image);
            assistants.get(i).setEffect(ca);
            cards.setTranslateY(assistants.get(i).getFitHeight() * 0.7);
            setGoUpEffectOnAssistantCard(assistants.get(i), i);
            setGoDownEffectOnAssistantCard(assistants.get(i), i);

        }
    }

    /**
     * Set up the front of the card when the player is on his own school
     */
    private void setAssistantsCardsFront(){
        cards.setTranslateY(assistants.get(0).getFitHeight() * 0.8);
        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            assistant.setImage(new Image("img/assistant/Assistente (" + (i + 1) + ").png"));

            ColorAdjust ca = new ColorAdjust();
            if(!rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(a -> a.getType().getCardTurnValue()).toList().contains(i + 1)){
                ca.setBrightness(-0.5);
                assistant.setEffect(ca);
                if(assistant.getOnMouseEntered() != null){
                    assistant.removeEventHandler(MouseEvent.MOUSE_ENTERED, assistant.getOnMouseEntered());
                }
                if(assistant.getOnMouseExited() != null){
                    assistant.removeEventHandler(MouseEvent.MOUSE_EXITED, assistant.getOnMouseExited());
                }
            }
            else{
                ca.setBrightness(0);
                assistant.setEffect(ca);
                setGoUpEffectOnAssistantCard(assistant, i);
                setGoDownEffectOnAssistantCard(assistant, i);
            }
        }
    }

    /**
     * makes the assistant cards playable by the player (adds effect on click of the player)
     */
    public void makeAssistantCardPlayable(){
        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            if(rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(a -> a.getType().getCardTurnValue()).toList().contains(i + 1)){
                int finalI = i;
                assistant.setOnMouseClicked(ActionEvent -> {
                    addActionValue(getAssistantTypeIndex(finalI + 1));
                    calculateNextAction();
                    for(ImageView a: assistants){
                        if(!a.equals(assistant)){
                            a.setOnMouseClicked(null);
                        }
                    }
                    assistant.setOnMouseClicked(null);
                });
            }
        }
    }

    /**
     * make the students in the entrance of the player own school selectable
     * and adds effect in case of hovering and click
     */
    public void makeStudentEntranceSelectable(){
        List<ImageView> entranceStudents = rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents();
        for(int i = 0; i < entranceStudents.size(); i++){
            if(entranceStudents.get(i).getEffect() == null && entranceStudents.get(i).getImage() != null) {
                setHoverEffect(entranceStudents.get(i), entranceStudents.get(i).getFitHeight() / 2 + 5);
                int finalI = i;
                entranceStudents.get(i).setOnMouseClicked(actionEvent -> {
                    for(ImageView cardAdvanced: advancedCards.stream().map(AdvancedCardController::getCardImage).toList()){
                        resetHoverEffect(cardAdvanced);
                        cardAdvanced.setOnMouseClicked(null);
                    }

                    for (ImageView student : entranceStudents) {
                        resetHoverEffect(student);
                    }
                    entranceStudents.get(finalI).setEffect(new DropShadow(entranceStudents.get(finalI).getFitHeight() / 2 + 5, Color.YELLOW));
                    addActionValue(finalI);

                    for (ImageView entranceStudent : entranceStudents) {
                        if (!entranceStudent.equals(entranceStudents.get(finalI))) {
                            entranceStudent.setOnMouseClicked(null);
                        }
                    }
                    if(playingAdvancedCard == BARD.ordinal()) {
                        if(actionValues.size() < 3){
                            makeStudentEntranceSelectable();
                        }
                        makeDiningTablesSelectable();
                    }
                    else if(playingAdvancedCard == JESTER.ordinal()){
                        AdvancedCardController jester = advancedCards.stream().filter(a -> a.getType().equals(JESTER)).toList().get(0);
                        for(int k = 0; k < jester.getObjectsSize(); k++){
                            resetHoverEffect(jester.getObjects().get(k));
                            jester.getObjects().get(k).setOnMouseClicked(null);
                        }
                        if(actionValues.size() - 1 < jester.getSelectedItem() * 2){
                            makeStudentEntranceSelectable();
                        }
                        else {
                            calculateNextAction();
                        }

                    }
                    else{
                        makeVisibleIslandsSelectable();
                        makeDiningHallSelectable();
                    }
                    entranceStudents.get(finalI).setOnMouseClicked(null);
                });
            }
        }
    }

    /**
     * Makes the all dining hall selectable handles over and clickable effect
     */
    public void makeDiningHallSelectable(){
        GridPane diningHall = rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall();
        diningHall.setOnMouseEntered(a -> diningHall.setStyle("-fx-background-color: rgba(255, 255, 0, 0.3);"));
        diningHall.setOnMouseExited(a -> diningHall.setStyle(null));
        diningHall.setOnMouseClicked(a -> {
            rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents().get(actionValues.get(0)).setEffect(null);
            addActionValue(gameBoard.getTerrain().getIslands().size());
            for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                island.setEffect(null);
                island.setOnMouseClicked(null);
                resetHoverEffect(island);
            }
            diningHall.setStyle(null);
            resetHoverEffect(diningHall);
            calculateNextAction();
            diningHall.setOnMouseClicked(null);
        });
    }

    /**
     * Makes the Islands selectable handles over and clickable effect
     */
    public void makeVisibleIslandsSelectable(){
        for (int i = 0; i < islands.size(); i++) {
            if (islands.get(i).isVisible()) {
                setHoverEffect(islands.get(i).getIsland(), islands.get(i).getIsland().getFitWidth() / 2);
                int finalI = i;
                islands.get(i).getIsland().setOnMouseClicked(a -> {
                    if(gameBoard.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_STUDENTS)){
                        rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents().get(actionValues.get(0)).setEffect(null);
                        rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall().setStyle(null);
                        rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall().setOnMouseClicked(null);
                        resetHoverEffect(rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall());
                    }
                    addActionValue(gameBoard.getTerrain().getIslands().indexOf(gameBoard.getTerrain().getIslandWithID(islands.get(finalI).getID())));
                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        resetHoverEffect(island);
                        island.setEffect(null);
                    }
                    calculateNextAction();

                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        if(!island.equals(islands.get(finalI).getIsland())) {
                            island.setOnMouseClicked(null);
                        }
                    }
                    islands.get(finalI).getIsland().setOnMouseClicked(null);
                });
            }
        }
    }

    /**
     * makes the next X islands clickable starting from mother nature position,
     * used to move mother nature based on the value of the assistant card selected
     * @param x the number of islands near mother nature to make visible
     */
    public void makeNextXIslandVisibleFromMotherNatureSelectable(int x){
        int startID = gameBoard.getTerrain().getIslands().stream().reduce((a, b) -> a.hasMotherNature() ? a : b).get().getID();
        int countedIsland = 0;
        for(int i = (startID + 1) % 12; countedIsland < x; i = (i + 1) % 12){
            if(islands.get(i).isVisible()){
                countedIsland++;
                int finalI = i;
                setHoverEffect(islands.get(i).getIsland(), islands.get(i).getIsland().getFitWidth() / 2 + 5);
                islands.get(i).getIsland().setOnMouseClicked(a -> {
                    addActionValue(islands.get(finalI).getID());
                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        island.setEffect(null);
                        resetHoverEffect(island);
                    }
                    calculateNextAction();
                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        island.setOnMouseClicked(null);
                    }
                });
            }
        }
    }

    /**
     * makes the cloud selectable only if they have students on them
     */
    public void makeCloudSelectable(){
        for(int i = 0; i < clouds.size(); i++){
            if(!gameBoard.getTerrain().getCloudCards().get(i).isEmpty()) {
                setHoverEffect(clouds.get(i).getCloudImage(), clouds.get(i).getCloudImage().getFitWidth() / 2 + 5);
                int finalI = i;
                clouds.get(i).getCloudImage().setOnMouseClicked(a -> {
                    for (CloudController cloud : clouds) {
                        resetHoverEffect(cloud.getCloudImage());
                    }
                    clouds.get(finalI).getCloudImage().setEffect(null);
                    addActionValue(finalI);
                    calculateNextAction();
                    for (CloudController cloud : clouds) {
                        if(!cloud.getCloudImage().equals(clouds.get(finalI).getCloudImage())){
                            cloud.getCloudImage().setOnMouseClicked(null);
                        }
                    }
                    clouds.get(finalI).getCloudImage().setOnMouseClicked(null);
                });
            }
        }
    }

    /**
     * makes advanced card selectable hover effect available for all to show the comment in the infoBox but
     * makes the card clickable only if the player has enough money
     */
    private void makeAdvancedCardSelectable(){
        for(AdvancedCardController card : advancedCards) {
            ImageView cardImage = card.getCardImage();
            String previousComment = comment.getText();
            Image previousCommentLogo = commentLogo.getImage();

            for(List<ImageView> objs: advancedCards.stream().map(AdvancedCardController::getObjects).toList()){
                for(ImageView obj: objs){
                    obj.setMouseTransparent(true);
                }
            }

            cardImage.setOnMouseEntered(a -> {
                cardImage.setEffect(new Glow(0.5));
                setComment(card.getType() + "\n" + card.getType().getEffect().replaceAll("\n", " "));
                setCommentLogo(infoLogo);
                setCommentBoxVisible();
            });
            cardImage.setOnMouseExited(a -> {
                cardImage.setEffect(new Glow(0));
                setComment(previousComment);
                setCommentLogo(previousCommentLogo);
            });
            if(card.getCost() <= gameBoard.getMoneys().get(gameBoard.getNames().indexOf(clientName))) {
                cardImage.setOnMouseClicked(a -> {
                    addActionValue(card.getType().ordinal());
                    for(List<ImageView> objs: advancedCards.stream().map(AdvancedCardController::getObjects).toList()){
                        for(ImageView obj: objs){
                            obj.setMouseTransparent(false);
                        }
                    }
                    card.playEffect(this);
                    resetAllClickableObjects();
                    card.playEffect(this);
                    for (AdvancedCardController c : advancedCards) {
                        if (!c.getType().equals(card.getType())) {
                            c.getCardImage().setOnMouseClicked(null);
                            c.getCardImage().setOnMouseExited(null);
                            c.getCardImage().setOnMouseEntered(null);
                        }
                    }
                    card.getCardImage().setOnMouseExited(null);
                    card.getCardImage().setOnMouseEntered(null);
                    card.getCardImage().setOnMouseClicked(null);
                });
            } else {
                cardImage.setOnMouseClicked(a -> {
                    setComment("Non hai abbastanza monete per giocare questa carta. Essa richiede " + card.getCost() + " monete per essere giocata e tu ne hai " + gameBoard.getMoneys().get(gameBoard.getNames().indexOf(clientName)) + ".");
                    setCommentLogo(notEnoughMoneyLogo);
                    setCommentBoxVisible();
                });
            }
        }
    }

    /**
     * makes dining tables selectable handles hover and clickable effect
     */
    protected void makeDiningTablesSelectable(){
        List<HBox> tables = getThisPlayerBoardController().getSchool().getTables();

        for (int i = 0; i < tables.size(); i++) {
            HBox table = tables.get(i);
            int finalI = i;
            table.setOnMouseEntered(a -> table.setStyle("-fx-background-color: rgba(255, 255, 0, " + 0.15 * (actionValues.size() - 1) + ");"));
            table.setOnMouseExited(a -> table.setStyle(null));
            table.setOnMouseClicked(a -> {
                addActionValue(finalI);
                List<ImageView> entranceStudents = rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents();
                for (ImageView entranceStudent : entranceStudents) {
                    entranceStudent.setOnMouseClicked(null);
                    entranceStudent.setOnMouseEntered(null);
                    entranceStudent.setOnMouseExited(null);
                }
                if(actionValues.size() == 3 || actionValues.size() == 5) {
                    for(ImageView entranceStudent : entranceStudents){
                        entranceStudent.setEffect(null);
                    }
                    for(HBox tb: tables){
                        if(!tb.equals(table)){
                            tb.setOnMouseClicked(null);
                            tb.setOnMouseEntered(null);
                            tb.setOnMouseExited(null);
                            tb.setEffect(null);
                            tb.setStyle(null);
                        }
                    }
                    table.setOnMouseEntered(null);
                    table.setStyle(null);
                    table.setOnMouseExited(null);
                    table.setEffect(null);
                    calculateNextAction();
                    table.setOnMouseClicked(null);
                }
                else{
                    makeDiningTablesSelectable();
                }
            });
        }
    }

    /**
     * Setter for playingAdvancedCard to the value given
     * @param value int to which the variable is set
     */
    protected void setPlayingAdvancedCard(int value){
        playingAdvancedCard = value;
    }

    /**
     * adds a value in the first position to the action value stack
     * @param value the int value to be added
     */
    protected void addActionValue(int value){
        actionValues.add(0, value);
    }

    /**
     * Calculates the degree needed to turn the board from the starting position to the final position
     * @param finalPos the int final position at which the board should be (0, 1, 2 or 3 based on the 4 side of the table)
     * @return an in representing the smallest number of degree needed to turn the table
     */
    private int getDegreeTurn(int finalPos){
        return Math.floorMod(finalPos - showedBoard.get(), 4) == 3 ? 90 : Math.floorMod(finalPos - showedBoard.get(), 4) == 2 ? 180 : Math.floorMod(finalPos - showedBoard.get(), 4) == 1 ? -90 : 0;
    }

    /**
     * Getter for client attribute
     * @return the client attribute
     * @see Client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Getter for the action values stack
     * @return a stack of integer the actionValues
     */
    public Stack<Integer> getActionValues() {
        return actionValues;
    }

    protected PlayerBoardController getThisPlayerBoardController(){
        return rotatingBoardController.getBoardOfPlayerWithName(clientName);
    }

    private void setRotatingButtonDisabled(boolean value){
        for(Button playerButton: playerBoardButtons){
            playerButton.setDisable(value);
        }
    }

    protected void setHoverEffect(Node node, double radius){
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.YELLOW);
        shadow.setRadius(radius);
        node.setOnMouseEntered(ActionEvent -> node.setEffect(shadow));
        node.setOnMouseExited(ActionEvent -> node.setEffect(null));
    }

    protected void resetHoverEffect(Node node){
        node.setOnMouseEntered(null);
        node.setOnMouseExited(null);
    }

    private void setGoUpEffectOnAssistantCard(ImageView assistant, int i){
        TranslateTransition moveUpEffect = new TranslateTransition(Duration.millis(500), assistant);
        assistant.setOnMouseEntered(ActionEvent -> new Thread(() -> {
            if(!isUp[i]){
                moveUpEffect.setByY(- assistant.getFitHeight() * 0.9);
                moveUpEffect.setOnFinished(a -> isUp[i] = true);
                Platform.runLater(moveUpEffect::play);
            }
        }).start());
    }

    private void setGoDownEffectOnAssistantCard(ImageView assistant, int i){
        TranslateTransition moveDownEffect = new TranslateTransition(Duration.millis(500), assistant);
        assistant.setOnMouseExited(ActionEvent -> new Thread(() -> {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                // Impossible to reach since the animation duration is ALWAYS > 0
                e.printStackTrace();
            }
            if(isUp[i]){
                moveDownEffect.setByY(assistant.getFitHeight() * 0.9);
                moveDownEffect.setOnFinished(a -> isUp[i] = false);
                Platform.runLater(moveDownEffect::play);
            }
        }).start());
    }

    private int getAssistantTypeIndex(int value){
        List<AssistantCardBoard> assistantsCard = rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards();
        for(int i = 0; i < assistantsCard.size(); i++){
            if(assistantsCard.get(i).getType().getCardTurnValue() == value){
                return i;
            }
        }
        //not reachable the card clickable are also the card playable so the value will always be found in the array
        return 0;
    }

    /**
     * Counts the steps needed for mother nature to move from where it is to the island selected with ID endID
     * @param endID the int representing the end island ID
     * @return an int representing the number of island to traverse
     */
    private int countStepFromMotherNatureToIslandWithID(int endID){
        int startID = gameBoard.getTerrain().getIslands().stream().reduce((a, b) -> a.hasMotherNature() ? a : b).get().getID();
        int count = 1;
        for(int i = (startID + 1) % 12; i != endID; i = (i + 1) % 12){
            count += gameBoard.getTerrain().getIslandWithID(i) != null ? 1 : 0;
        }
        return count;
    }
    public void setComment(String message){
        Platform.runLater(() -> comment.setText(message));
    }

    /**
     * Setter for the comment logo
     * @param logoToSet Image to set in the comment logo
     */
    public void setCommentLogo(Image logoToSet) { commentLogo.setImage(logoToSet); }

    /**
     * Set the visibility of the comment box to true
     */
    public void setCommentBoxVisible() { commentBox.setVisible(true); }

    /**
     * Set the visibility if the comment box to false
     */
    public void setCommentBoxNotVisible() { commentBox.setVisible(false); }

    /**
     * Getter for the Image of the error Logo
     * @return the Image of the error Logo
     */
    public Image getErrorLogo() {
        return errorLogo;
    }

    /**
     * Reset all the objects in the pane with no hover and click effect
     */
    public void resetAllClickableObjects(){
        List<ImageView> objects = new ArrayList<>();
        objects.addAll(rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents());
        objects.addAll(islands.stream().map(IslandController::getIsland).toList());
        objects.addAll(clouds.stream().map(CloudController::getCloudImage).toList());

        for(ImageView o: objects){
            o.setStyle(null);
            o.setOnMouseClicked(null);
            o.setOnMouseEntered(null);
            o.setOnMouseExited(null);
            o.setEffect(null);
        }
    }
}
