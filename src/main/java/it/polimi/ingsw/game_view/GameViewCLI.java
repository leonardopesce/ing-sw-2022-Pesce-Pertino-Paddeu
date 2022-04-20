package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.*;
import it.polimi.ingsw.server.LobbyInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;
import static it.polimi.ingsw.game_view.GameViewClient.InputStateMachine.*;

public class GameViewCLI implements GameViewClient{
    private final Scanner input;
    private int rangeA, rangeB;
    private ClientMessageObserverHandler msgHandler;
    private GameBoard board;
    private Client client;

    public GameViewCLI(Client client) {
        this.client = client;
        input = new Scanner(System.in);
        msgHandler = new ClientMessageObserverHandler(this);
        msgHandler.addObserver(client);
    }

    @Override
    public void askName() {
        System.out.println(GameViewClient.ASK_NAME_QUESTION);
        msgHandler.notifier(ASK_NAME, client.setName(input.nextLine()));
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
                whileInputNotContainedIn(Arrays.asList("e", "n")).equals("e")));
    }

    @Override
    public void askJoiningAction() {
        System.out.println(GameViewClient.ASK_JOINING_ACTION_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(ASK_JOINING_ACTION, whileInputNotIntegerInRange(0,1)));
    }

    @Override
    public void askLobbyToJoin(Object listOfLobbyInfos) {
        System.out.println(GameViewClient.ASK_LOBBY_TO_JOIN_QUESTION);
        int i = 0;
        for(LobbyInfo lobby : (List<LobbyInfo>)listOfLobbyInfos) {
            System.out.println(i + ". " + lobby.getLobbyName() + " | " + (lobby.isFull() ? Printable.TEXT_RED + lobby.getCurrentLobbySize() + "/" + lobby.getLobbyMaxSize() + " Lobby Full" + Printable.TEXT_RESET : Printable.TEXT_GREEN + lobby.getCurrentLobbySize() + "/" + lobby.getLobbyMaxSize() + Printable.TEXT_RESET));
            i++;
        }
        client.asyncWriteToSocket(new CommunicationMessage(ASK_LOBBY_TO_JOIN, whileInputNotContainedIn(((List<LobbyInfo>)listOfLobbyInfos).stream().map(LobbyInfo::getLobbyName).toList())));
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
        msgHandler.updateBoardMessage(board);
        msgHandler.setState(PLANNING_PHASE_START);
        asyncReadInput();
    }

    @Override
    public void reaskAssistant() {
        System.out.println("Assistant not playable pick another one");
        msgHandler.setState(PLANNING_PHASE_START);
        msgHandler.setActionSent(true);
    }

    @Override
    public void updateBoard(GameBoard board) {
        System.out.println(board);
    }

    public void asyncReadInput(){
        new Thread(() -> {
            while(client.isActive()){
                if(!board.getCurrentlyPlaying().equals(client.getName())){
                    try {
                        if(System.in.available() > 0) {
                            input.nextLine();
                            displayNotYourTurn();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(!msgHandler.isActionSent()) {
                    printStateMachine();
                    actionStateMachine(whileInputNotIntegerInRange(rangeA, rangeB));
                }
            }
        }).start();
    }

    private void printStateMachine(){
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        SchoolBoard school = board.getSchools().get(board.getNames().indexOf(client.getName()));

        switch (msgHandler.getState()) {
            case PLANNING_PHASE_START -> {
                rangeA = 0;
                rangeB = playerDeck.getCards().size() - 1;
                System.out.println("Select an assistant card to play (use value from 0 to " + rangeB
                        + " to select the card):\n" + playerDeck);
                msgHandler.setState(SELECT_ASSISTANT_CARD_SEND_MESSAGE);
            }
            case MOVING_STUDENT_PHASE_START -> {
                rangeA = 0;
                rangeB = school.getEntrance().size() - 1;
                System.out.println("Please select a student to move (use number from 0 to " + rangeB
                        + " starting counting from left to right and top to bottom");
                msgHandler.setState(MOVE_STUDENT_SEND_MESSAGE);
            }
            case MOVE_MOTHER_NATURE_START -> {
                rangeA = 1;
                rangeB = playerDeck.getDiscardedCard().getPossibleSteps();
                System.out.println("How many step would you like to move mother nature (use a number between 1 and " + rangeB + ")");
                msgHandler.setState(MOVE_MOTHER_NATURE_SEND_MESSAGE);
            }
            case CHOOSE_CLOUD_CARD_START -> {
                rangeA = 0;
                rangeB = board.getTerrain().getCloudCards().size() - 1;
                System.out.println("Select a Cloud from where to pick student (use number from 0 to" + rangeB
                        + " to select the cloud):\n");
                msgHandler.setState(CHOOSE_CLOUD_CARD_SEND_MESSAGE);
            }
        }
    }

    protected void actionStateMachine(int selection){
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        SchoolBoard school = board.getSchools().get(board.getNames().indexOf(client.getName()));
        List<IslandBoard> islands = board.getTerrain().getIslands();
        switch(msgHandler.getState()) {
            case PLAY_ADVANCED_CARD:
                System.out.println("Select an advanced card " + board.getTerrain().getAdvancedCard());
                int selectedCard = whileInputNotIntegerInRange(0, 2);
                System.out.println("You played: " + board.getTerrain().getAdvancedCard().get(selectedCard));
                //client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction()));
                break;

            case SELECT_ASSISTANT_CARD_SEND_MESSAGE:
                System.out.println("You selected: " + playerDeck.getCards().get(selection).getName());
                client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAssistantCardAction(client.getName(), selection)));
                msgHandler.setActionSent(true);
                break;

            case MOVE_STUDENT_SEND_MESSAGE:
                rangeA = 0;
                rangeB = islands.size();
                System.out.println("You selected student " + selection + GameBoard.getColorString(school.getEntrance().get(selection))
                        + Printable.STUDENT + Printable.TEXT_RESET);
                System.out.println("Please select where to move the student (use number from 0 to " +
                        rangeB + " to select an island and number " + rangeB + " to select the dining hall)");

                String read = input.nextLine();
                while(read.isEmpty() || !read.chars().allMatch(Character::isDigit) ||
                        Integer.parseInt(read) < rangeA || Integer.parseInt(read) > rangeB){
                    System.out.println("Not a number or not in the range given");
                    read = input.nextLine();
                }

                if(Integer.parseInt(read) == islands.size()){
                    System.out.println("Dining hall selected");
                    client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveStudentToDiningHallAction(client.getName(), selection)));
                }
                else {
                    System.out.println("Island " + selection + " selected");
                    client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveStudentToIslandAction(client.getName(), selection, Integer.parseInt(read))));
                }
                msgHandler.setActionSent(true);
                break;

            case MOVE_MOTHER_NATURE_SEND_MESSAGE:
                client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveMotherNatureAction(client.getName(), selection)));
                msgHandler.setActionSent(true);
                break;

            case CHOOSE_CLOUD_CARD_SEND_MESSAGE:
                if(board.getTerrain().getCloudCards().get(selection).isEmpty()){
                    System.out.println("You selected an empty cloud, please pick another one");
                }
                else {
                    client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new ChooseCloudCardAction(client.getName(), selection)));
                    msgHandler.setActionSent(true);
                }
        }
    }



    @Override
    public void displayNotYourTurn(){
        System.out.println("PLEASE WAIT FOR YOUR TURN");
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
        while((read.isEmpty() || !read.chars().allMatch(Character::isDigit) ||
                Integer.parseInt(read) < a || Integer.parseInt(read) > b) &&
                (!board.isExpertMode() || !read.equals("play"))){
            System.out.println("Not a number or not in the range given");
            read = input.nextLine();
        }
        if(read.equals("play")){
            msgHandler.setState(PLAY_ADVANCED_CARD);
            return 0;
        }
        return Integer.parseInt(read);
    }

    private String whileInputNotContainedIn(List<String> container){
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

    public GameBoard getBoard() {
        return board;
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public ClientMessageObserverHandler getMessageObserver() {
        return msgHandler;
    }
}
