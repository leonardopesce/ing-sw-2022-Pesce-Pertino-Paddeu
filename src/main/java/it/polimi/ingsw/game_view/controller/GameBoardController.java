package it.polimi.ingsw.game_view.controller;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.GamePhase;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.IslandBoard;
import it.polimi.ingsw.network.client.Client;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.GAME_ACTION;

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
    private GameBoard gameBoard;
    private final Stack<Integer> actionValues = new Stack<>();
    private final List<CloudController> clouds = new ArrayList<>();
    @FXML
    ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10;
    @FXML
    private Button player1Board, player2Board, player3Board, player4Board;
    @FXML
    private Label gamePhaseLabel;
    @FXML
    private StackPane mainPane;
    @FXML
    private HBox cards, cloudHBox;
    @FXML
    private GridPane infoBox;
    @FXML
    private IslandController island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller;
    @FXML
    private RotatingBoardController rotatingBoardController;
    @FXML
    private CloudController cloud0Controller, cloud1Controller, cloud2Controller, cloud3Controller;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clouds.addAll(Arrays.asList(cloud0Controller, cloud1Controller, cloud2Controller, cloud3Controller));
        playerBoardButtons.addAll(Arrays.asList(player1Board, player2Board, player3Board, player4Board));
        islands.addAll(Arrays.asList(island0Controller, island1Controller, island2Controller, island3Controller, island4Controller, island5Controller, island6Controller, island7Controller, island8Controller, island9Controller, island10Controller, island11Controller));

        for(int i = 0; i < islands.size(); i++){
            islands.get(i).setID(i);
        }

        mainPane.setBackground(new Background(new BackgroundImage(new Image("img/table.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1, 1, false, false, true, true))));

        assistants.addAll(Arrays.asList(assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10));

        rotateTransition.setAxis(Rotate.Z_AXIS);
        rotateTransition.setCycleCount(1);
        rotateTransition.setDuration(Duration.millis(1500));
        rotateTransition.setAutoReverse(false);
        rotateTransition.setNode(rotatingBoardController.getPane());

        infoBox.setBorder(new Border(new BorderStroke(Color.rgb(255, 200, 0), BorderStrokeStyle.SOLID, new CornerRadii(0, 15, 0, 0, false), BorderStroke.THICK)));
        infoBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 230, 130, 0.7), new CornerRadii(0, 15, 0, 0, false), Insets.EMPTY)));
    }

    public void setClient(Client client) {
        this.client = client;
        clientName = client.getName();
        showedBoard.addListener((observable, oldValue, newValue) -> new Thread(() -> setUpDecks((Integer) newValue)).start());
    }

    public void setClientName(String name){
        //TODO delete, just for testing purpose
        this.clientName = name;
        showedBoard.addListener((observable, oldValue, newValue) -> new Thread(() -> setUpDecks((Integer) newValue)).start());

    }

    private void calculateNextAction(){
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION,
            switch (gameBoard.getPhase()){
                case PLANNING_PHASE ->  new PlayAssistantCardAction(clientName, actionValues.pop());
                case ACTION_PHASE_MOVING_STUDENTS -> actionValues.get(0) == gameBoard.getTerrain().getIslands().size() ?
                        new MoveStudentToDiningHallAction(clientName, actionValues.pop()) :
                        new MoveStudentToIslandAction(clientName, actionValues.pop(), actionValues.pop());
                case ACTION_PHASE_MOVING_MOTHER_NATURE -> new MoveMotherNatureAction(clientName, countStepFromMotherNatureToIslandWithID(actionValues.pop()));
                case ACTION_PHASE_CHOOSING_CLOUD -> new ChooseCloudCardAction(clientName, actionValues.pop());
                default -> throw new IllegalStateException("Unexpected value: " + gameBoard.getPhase());
        }));
        actionValues.clear();
    }

    public void updateBoard(GameBoard board){
        Platform.runLater(() -> {
            gameBoard = board;
            rotatingBoardController.update(board);
            gamePhaseLabel.setText(board.getPhase().toString());
            if (firstTime) {
                firstTime = false;
                for (int i = 0; i < 4; i++) {
                    if (i < board.getNames().size()) {
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
                    } else {
                        rotatingBoardController.getBoardX(i).hide();
                        playerBoardButtons.get(i).setDisable(true);
                        playerBoardButtons.get(i).setVisible(false);
                    }
                }
                setUpDecks(showedBoard.get());

                for(int i = 4; i > board.getTerrain().getCloudCards().size(); i--){
                    clouds.remove(i - 1);
                    cloudHBox.getChildren().remove(i - 1);
                }

            }
            for (int i = 0; i < board.getNames().size(); i++) {
                if (playerBoardButtons.get(i).getText().equals(clientName)) {
                    ColorAdjust ca = new ColorAdjust();
                    for(int k = 0; k < assistants.size(); k++){
                        if(!rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(AssistantType::getCardTurnValue).toList().contains(k + 1)) {
                            ca.setBrightness(-0.5);
                            assistants.get(k).setEffect(ca);
                        }
                    }
                    playerBoardButtons.get(i).fire();
                }
            }

            for (IslandController island: islands) {
                if(board.getTerrain().getIslands().stream().map(IslandBoard::getID).toList().contains(island.getID())){
                    island.unHide();
                    island.update(board.getTerrain().getIslandWithID(island.getID()));
                }
                else{
                    island.hide();
                }
            }

            for(int i = 0; i < board.getTerrain().getCloudCards().size(); i++){
                clouds.get(i).update(board.getTerrain().getCloudCards().get(i));
            }

            if (board.getCurrentlyPlaying().equals(clientName)) {
                if (board.getPhase().equals(GamePhase.PLANNING_PHASE)) {
                    makeAssistantCardPlayable();
                }
                else if (board.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_STUDENTS)) {
                    makeStudentEntranceSelectable();
                }
                else if(board.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_MOTHER_NATURE)){
                    makeNextXIslandVisibleFromMotherNatureSelectable(board.getDecks().get(board.getNames().indexOf(clientName)).getDiscardedCard().getPossibleSteps());
                }
                else if(board.getPhase().equals(GamePhase.ACTION_PHASE_CHOOSING_CLOUD)){
                    makeCloudSelectable();
                }
            } else {
                gamePhaseLabel.setText("NOT YOUR TURN");
            }
        });
    }

    private void setUpDecks(int pos){
        if(rotatingBoardController.getBoardX(pos).getName().getText().equals(clientName)){
            setAssistantsCardsFront();
        }
        else {
            setAssistantsCardsRetro(new Image(rotatingBoardController.getBoardX(pos).getDeckBoard().getDeckType().getPath()));
        }
    }

    private void setAssistantsCardsRetro(Image image){
        ColorAdjust ca = new ColorAdjust();
        for(int i = 0; i < assistants.size(); i++){
            assistants.get(i).setImage(image);
            assistants.get(i).setEffect(ca);
            cards.setTranslateY(assistants.get(i).getFitHeight() * 0.8);
            setGoUpEffectOnAssistantCard(assistants.get(i), i);
            setGoDownEffectOnAssistantCard(assistants.get(i), i);

        }
    }

    private void setAssistantsCardsFront(){
        cards.setTranslateY(assistants.get(0).getFitHeight() * 0.8);
        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            assistant.setImage(new Image("img/assistant/Assistente (" + (i + 1) + ").png"));

            ColorAdjust ca = new ColorAdjust();
            if(!rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(AssistantType::getCardTurnValue).toList().contains(i + 1)){
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

    public void makeAssistantCardPlayable(){
        for(int i = 0; i < assistants.size(); i++){
            ImageView assistant = assistants.get(i);
            if(rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards().stream().map(AssistantType::getCardTurnValue).toList().contains(i + 1)){
                int finalI = i;
                assistant.setOnMouseClicked(ActionEvent -> {
                    actionValues.add(0, getAssistantTypeIndex(finalI + 1));
                    for(ImageView a: assistants){
                        a.setOnMouseClicked(null);
                    }
                    calculateNextAction();
                });
            }
        }
    }


    public void makeStudentEntranceSelectable(){
        List<ImageView> entranceStudents = rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents();
        for(int i = 0; i < entranceStudents.size(); i++){
            setHoverEffect(entranceStudents.get(i), entranceStudents.get(i).getFitHeight() / 2 + 5);
            int finalI = i;
            entranceStudents.get(i).setOnMouseClicked(actionEvent -> {
                for(int k = 0; k < entranceStudents.size(); k++) {
                    resetHoverEffect(entranceStudents.get(k));

                }
                entranceStudents.get(finalI).setEffect(new DropShadow(entranceStudents.get(finalI).getFitHeight() / 2 + 5, Color.YELLOW));
                actionValues.add(0, finalI);
                System.out.println("YOU selected student number " + finalI + " color " + gameBoard.getSchools().get(gameBoard.getNames().indexOf(clientName)));
                makeVisibleIslandsSelectable();
                makeDiningHallSelectable();

            });
        }
    }

    public void makeDiningHallSelectable(){
        GridPane diningHall = rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall();
        diningHall.setOnMouseEntered(a -> diningHall.setStyle("-fx-background-color: rgba(255, 255, 0, 0.3);"));
        diningHall.setOnMouseExited(a -> diningHall.setStyle(null));
        diningHall.setOnMouseClicked(a -> {
            rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents().get(actionValues.get(0)).setEffect(null);
            actionValues.add(0, gameBoard.getTerrain().getIslands().size());
            for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                island.setEffect(null);
                resetHoverEffect(island);
            }
            diningHall.setStyle(null);
            resetHoverEffect(diningHall);
            calculateNextAction();
        });
    }

    public void makeVisibleIslandsSelectable(){
        for (int i = 0; i < islands.size(); i++) {
            if (islands.get(i).isVisible()) {
                setHoverEffect(islands.get(i).getIsland(), islands.get(i).getIsland().getFitWidth() / 2);
                int finalI = i;
                islands.get(i).getIsland().setOnMouseClicked(a -> {
                    if(gameBoard.getPhase().equals(GamePhase.ACTION_PHASE_MOVING_STUDENTS)){
                        rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getEntranceStudents().get(actionValues.get(0)).setEffect(null);
                        rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall().setStyle(null);
                        resetHoverEffect(rotatingBoardController.getBoardOfPlayerWithName(clientName).getSchool().getDiningHall());
                    }
                    actionValues.add(0, islands.get(finalI).getID());
                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        island.setEffect(null);
                        resetHoverEffect(island);
                    }
                    calculateNextAction();
                });
            }
        }
    }

    public void makeNextXIslandVisibleFromMotherNatureSelectable(int x){
        int startID = gameBoard.getTerrain().getIslands().stream().reduce((a, b) -> a.hasMotherNature() ? a : b).get().getID();
        int countedIsland = 0;
        for(int i = (startID + 1) % 12; countedIsland < x; i = (i + 1) % 12){
            if(islands.get(i).isVisible()){
                countedIsland++;
                int finalI = i;
                setHoverEffect(islands.get(i).getIsland(), islands.get(i).getIsland().getFitWidth() / 2 + 5);
                islands.get(i).getIsland().setOnMouseClicked(a -> {
                    actionValues.add(0, islands.get(finalI).getID());
                    for(ImageView island: islands.stream().map(IslandController::getIsland).toList()){
                        island.setEffect(null);
                        resetHoverEffect(island);
                    }
                    calculateNextAction();
                });
            }
        }
    }

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
                    actionValues.add(0, finalI);
                    calculateNextAction();
                });
            }
        }
    }

    private int getDegreeTurn(int finalPos){
        return Math.floorMod(finalPos - showedBoard.get(), 4) == 3 ? 90 : Math.floorMod(finalPos - showedBoard.get(), 4) == 2 ? 180 : Math.floorMod(finalPos - showedBoard.get(), 4) == 1 ? -90 : 0;
    }

    private void setRotatingButtonDisabled(boolean value){
        for(Button playerButton: playerBoardButtons){
            playerButton.setDisable(value);
        }
    }

    private void setHoverEffect(Node node, double radius){
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.YELLOW);
        shadow.setRadius(radius);
        node.setOnMouseEntered(ActionEvent -> node.setEffect(shadow));
        node.setOnMouseExited(ActionEvent -> node.setEffect(null));
    }

    private void resetHoverEffect(Node node){
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
                Thread.sleep(500);
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
        List<AssistantType> assistantsCard = rotatingBoardController.getBoardOfPlayerWithName(clientName).getDeckBoard().getCards();
        for(int i = 0; i < assistantsCard.size(); i++){
            if(assistantsCard.get(i).getCardTurnValue() == value){
                return i;
            }
        }
        //not reachable the card clickable are also the card playable so the value will always be found in the array
        return 0;
    }

    private int countStepFromMotherNatureToIslandWithID(int endID){
        int startID = gameBoard.getTerrain().getIslands().stream().reduce((a, b) -> a.hasMotherNature() ? a : b).get().getID();
        int count = 1;
        for(int i = (startID + 1) % 12; i != endID; i = (i + 1) % 12){
            count += gameBoard.getTerrain().getIslandWithID(i) != null ? 1 : 0;
        }
        return count;
    }
}
