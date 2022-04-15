package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class GameViewCLI extends GameViewClient{
    private final Scanner input;

    public GameViewCLI(Client client) {
        super(client);
        input = new Scanner(System.in);
    }

    @Override
    public void askName() {
        System.out.println(GameViewClient.ASK_NAME_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(ASK_NAME, client.setName(input.nextLine())));
    }

    @Override
    public void reaskName() {
        System.out.println(GameViewClient.REASK_NAME_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(REASK_NAME, client.setName(input.nextLine())));
    }

    @Override
    public void askDeck(Object decksAvailable) {
        System.out.println(GameViewClient.ASK_DECK_TYPE_QUESTION + decksToString((List<?>)decksAvailable));
        client.asyncWriteToSocket(new CommunicationMessage(
                ASK_DECK,
                ((List<?>) decksAvailable).get(whileInputNotIntegerInRange(0, ((List<?>) decksAvailable).size() - 1))
        ));
    }

    @Override
    public void askGameType() {
        System.out.println(GameViewClient.ASK_GAME_TYPE_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(
                ASK_GAME_TYPE,
                whileInputNotContainedInt(Arrays.asList("e", "n")).equals("e")));
    }

    @Override
    public void askPlayerNumber() {
        System.out.println(GameViewClient.ASK_PLAYER_NUMBER_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(
                ASK_GAME_TYPE,
                whileInputNotIntegerInRange(2, 4)));
    }

    @Override
    public void gameReady(GameBoard board){
        this.board = board;
        System.out.println(board);
        asyncReadInput();
    }

    @Override
    public void updateBoard(GameBoard board) {
        System.out.println(board);
    }

    public void asyncReadInput(){
        new Thread(() -> {
            while(client.isActive()){

                if(!actionSent && board.getCurrentlyPlaying().equals(client.getName())) {
                    switch (board.getPhase()) {
                        case PLANNING_PHASE -> playAssistantCardAction();
                        case ACTION_PHASE_MOVING_STUDENTS -> movingStudentAction();
                        case ACTION_PHASE_MOVING_MOTHER_NATURE -> moveMotherNatureAction();
                        case ACTION_PHASE_CHOOSING_CLOUD -> chooseCloudCardAction();
                    }
                    actionSent = true;
                }
                else {
                    try {
                        if(System.in.available() > 0){
                            input.nextLine();
                            System.out.println("PLEASE WAIT FOR YOUR TURN");
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void displayYourTurn() {
        System.out.println("IT'S YOUR TURN!");
    }

    @Override
    public void displayExpertMode() {
        System.out.println("The game is played in expert mode to play a special card, write \"play\" in any moment");
        System.out.println(board.getTerrain().getAdvancedCard());
    }

    private int whileInputNotIntegerInRange(int a, int b){
        String read = input.nextLine();
        while(read.isEmpty() || !read.chars().allMatch(Character::isDigit) ||
                Integer.parseInt(read) < a || Integer.parseInt(read) > b){
            System.out.println("Not a number or not in the range given");
            read = input.nextLine();
        }
        return Integer.parseInt(read);
    }

    private String whileInputNotContainedInt(List<String> container){
        String read = input.nextLine();
        while(!container.contains(read)){
            System.out.println("Selection not available please follow instruction");
            read = input.nextLine();
        }
        return read;
    }

    private String decksToString(List<?> decks){
        String msg = "";
        for(int i = 0; i < decks.size(); i++){
            msg = msg.concat("\n" + i + " = " + ((DeckType)decks.get(i)).getName());
        }
        return msg;
    }

    private void playAssistantCardAction(){
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        int selectedCard;
        int b = playerDeck.getCards().size() - 1;
        System.out.println("Select an assistant card to play (use value from 0 to " + b + " to select the card):\n");
        System.out.println(playerDeck);
        selectedCard = whileInputNotIntegerInRange(0, b);
        System.out.println("You selected: " + playerDeck.getCards().get(selectedCard).getName());
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAssistantCardAction(client.getName(), selectedCard)));
    }

    private void movingStudentAction() {
        SchoolBoard school = board.getSchools().get(board.getNames().indexOf(client.getName()));
        int b = school.getEntrance().size() - 1;
        System.out.println("Please select a student to move (use number from 0 to " +
                b + " starting counting from left to right and top to bottom");
        int selectedStudent = whileInputNotIntegerInRange(0, b);
        System.out.println("You selected student " + selectedStudent
                + GameBoard.getColorString(school.getEntrance().get(selectedStudent))
                + Printable.STUDENT + Printable.TEXT_RESET);

        List<IslandBoard> islands = board.getTerrain().getIslands();
        b = islands.size();
        System.out.println("Please select where to move the student (use number from 0 to " +
                (b - 1) + " to select an island and number " + b + " to select the dining hall");
        int selectedPlace = whileInputNotIntegerInRange(0, b);
        if(selectedPlace == islands.size()){
            System.out.println("Dining hall selected");
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveStudentToDiningHallAction(client.getName(), selectedStudent)));
        }
        else {
            System.out.println("Island " + selectedPlace + " selected");
            client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveStudentToIslandAction(client.getName(), selectedStudent, selectedPlace)));
        }
    }

    private void moveMotherNatureAction() {
        DeckBoard deck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        int b = deck.getDiscardedCard().getPossibleSteps();
        System.out.println("How many step would you like to move mother nature (use a number between 1 and " + b + ")");
        int step = whileInputNotIntegerInRange(1, b);
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveMotherNatureAction(client.getName(), step)));
    }

    private void chooseCloudCardAction(){
        int b = board.getTerrain().getCloudCards().size() - 1;
        System.out.println("Select a Cloud from where to pick student (use number from 0 to" + b + " to select the cloud):\n");
        int selectedCard = whileInputNotIntegerInRange(0, b);
        while(board.getTerrain().getCloudCards().get(selectedCard).isEmpty()){
            System.out.println("You selected an empty cloud, please pick another one");
            selectedCard = whileInputNotIntegerInRange(0, b);
        }
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new ChooseCloudCardAction(client.getName(), selectedCard)));
    }
}
