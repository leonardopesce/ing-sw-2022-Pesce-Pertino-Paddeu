package it.polimi.ingsw.game_view;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientMessageObserverHandler;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.*;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;
import static it.polimi.ingsw.game_view.GameViewClient.InputStateMachine.*;

/**
 * Represents the CLI. Handles all the commands and updates when using the command line interface.
 */
public class GameViewCLI implements GameViewClient{
    private final Scanner input;
    private int rangeA, rangeB;
    private ClientMessageObserverHandler msgHandler;
    private InputStateMachine previousStateBeforeAdvancedCardPlayed;
    private GameBoard board;
    private final Client client;
    private boolean gameStarted = false;

    /**
     * @param ip the ip of the server on which the player wants to connect.
     * @param port the port of the server on which the player wants to connect.
     */
    public GameViewCLI(String ip, int port) {
        // Running a new client object with the specified ip and port.
        client = new Client(ip, port);
        new Thread(() -> {
            try {
                client.run();
            } catch (IOException e) {
                Logger.ERROR("Unable to connect to server. Exit...", e.getMessage());
            }
        }).start();
        input = new Scanner(System.in);
        msgHandler = new ClientMessageObserverHandler(this);
        client.addObserver(msgHandler);
    }

    @Override
    public void askName() {
        System.out.println(GameViewClient.ASK_NAME_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, client.setName(input.nextLine())));
    }

    @Override
    public void reaskName() {
        System.out.println(GameViewClient.REASK_NAME_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(NAME_MESSAGE, client.setName(input.nextLine())));
    }

    @Override
    public void askJoiningAction() {
        System.out.println(GameViewClient.ASK_JOINING_ACTION_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(JOINING_ACTION_INFO, whileInputNotIntegerInRange(0,1)));
    }

    @Override
    public void displayNoLobbiesAvailable() {
        Logger.INFO("There are no lobbies available. Please, chose another option.");
    }

    @Override
    public void askPlayerNumber() {
        System.out.println(GameViewClient.ASK_PLAYER_NUMBER_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(
                NUMBER_OF_PLAYER_INFO,
                whileInputNotIntegerInRange(2, 4)));
    }

    @Override
    public void askGameType() {
        System.out.println(GameViewClient.ASK_GAME_TYPE_QUESTION);
        client.asyncWriteToSocket(new CommunicationMessage(
                GAME_TYPE_INFO,
                whileInputNotContainedIn(Arrays.asList("e", "n")).equals("e")));
    }

    @Override
    public void displayLobbyJoined(Object lobbyInfos) {
        Logger.INFO("Current members:");
        for(String memberName : ((LobbyInfo)lobbyInfos).getLobbyMembers()) {
            if(memberName.equals(((LobbyInfo)lobbyInfos).getLobbyName())) Logger.INFO("\uD83D\uDC51" + " " + memberName);
            else Logger.INFO(memberName);

        }
    }

    @Override
    public void displayIsChoosingDeckType(Object playerNameWhoIsChosingTheDeck) {
        Logger.INFO(playerNameWhoIsChosingTheDeck + " is choosing the deck type.");
    }

    @Override
    public void askDeck(Object availableDecks) {
        System.out.println(GameViewClient.ASK_DECK_TYPE_QUESTION + decksToString((List<?>)availableDecks));
        client.asyncWriteToSocket(new CommunicationMessage(
                DECK_TYPE_MESSAGE,
                ((List<?>) availableDecks).get(whileInputNotIntegerInRange(0, ((List<?>) availableDecks).size() - 1))
        ));
    }

    @Override
    public void askLobbyToJoin(Object listOfLobbyInfos) {
        System.out.println(GameViewClient.ASK_LOBBY_TO_JOIN_QUESTION);
        int i = 0;
        for(LobbyInfo lobby : (List<LobbyInfo>)listOfLobbyInfos) {
            System.out.println(i + ". " + lobby);
            i++;
        }
        client.asyncWriteToSocket(new CommunicationMessage(LOBBY_TO_JOIN_INFO, whileInputNotContainedIn(((List<LobbyInfo>)listOfLobbyInfos).stream().map(LobbyInfo::getLobbyName).toList())));
    }


    @Override
    public void gameReady(GameBoard board){
        msgHandler.updateBoardMessage(board);
        msgHandler.setState(PLANNING_PHASE_START);
        gameStarted = true;
        asyncReadInput();
    }

    @Override
    public void reaskAssistant() {
        System.out.println("Assistant not playable pick another one");
        msgHandler.setState(PLANNING_PHASE_START);
        msgHandler.setActionSent(false);
    }

    @Override
    public void updateBoard(GameBoard board) {
        System.out.println(board);
    }

    @Override
    public void onPlayerDisconnection(String playerWhoMadeTheLobbyClose) {
        Logger.ERROR(playerWhoMadeTheLobbyClose + "'s connection has been interrupted. The lobby will now close and you will be disconnected from the server.", "Player disconnection");
    }

    @Override
    public void displayErrorMessage(String errorMsg, String errorType, GameBoard boardToUpdate) {
        Logger.ERROR(errorMsg, errorType);
        msgHandler.updateBoardMessage(board);
    }

    @Override
    public void displayNotYourTurn(){
        Logger.ERROR("("+ board.getCurrentlyPlaying() + " is playing). Wait your turn to play.", "Other player turn");
    }

    @Override
    public void displayYourTurn() {
        Logger.INFO("It's your turn now!");
    }

    @Override
    public void displayOtherPlayerTurn(String otherPlayerName) {
        Logger.INFO("It's " + otherPlayerName + "'s turn. Soon you will be able to play!");
    }

    @Override
    public void displayExpertMode() {
        for(AdvancedCardBoard card : board.getTerrain().getAdvancedCard()) System.out.println(card);
        if(client.getName().equals(board.getCurrentlyPlaying())) System.out.println("The game is played in expert mode to play a special card, write \"play\" in any moment");
    }

    @Override
    public void displayEndGame(CommunicationMessage.MessageType condition){
        if(condition == YOU_WIN){
            Logger.INFO("You have won the match!");
        }
        else if(condition == YOU_LOSE){
            Logger.INFO("You lost the match!");
        }
        else {
            Logger.INFO("The match finished in a draw.");
        }
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }

    @Override
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    @Override
    public Client getClient() {
        return client;
    }

    /**
     * Asynchronously reads from the command line.
     *
     * <p>
     *     If it's not the player turn, an error message is displayed, otherwise the state machine is printed.
     * </p>
     */
    public void asyncReadInput(){
        new Thread(() -> {
            while(client.isActive()){
                // If it's not the player turn, an error message is printed when the user types something in the command line.
                if(!board.getCurrentlyPlaying().equals(client.getName())){
                    try {
                        if(System.in.available() > 0) {
                            input.nextLine();
                            displayNotYourTurn();
                        }
                    } catch (IOException e) {
                        Logger.ERROR("Client input stream is not available anymore.", e.getMessage());
                    }
                }
                // If the action has not been sent yet, then we print the instructions to build the next required command
                // in order to play the current game phase.
                else if(!msgHandler.isActionSent()) {
                    printStateMachine();
                    actionStateMachine(whileInputNotIntegerInRange(rangeA, rangeB));
                }
            }
        }).start();
    }

    /**
     * Based on the FSM status, it prints the instructions for the required command the user has to use in
     * that phase.
     */
    private void printStateMachine(){
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        SchoolBoard school = board.getSchools().get(board.getNames().indexOf(client.getName()));

        switch (msgHandler.getState()) {
            case PLANNING_PHASE_START -> {
                rangeA = 0;
                rangeB = playerDeck.getCards().size() - 1;
                System.out.println(playerDeck + "\nSelect an assistant card to play (use value from 0 to " + rangeB
                        + " to select the card): ");
                msgHandler.setState(SELECT_ASSISTANT_CARD_SEND_MESSAGE);
            }
            case MOVING_STUDENT_PHASE_START -> {
                rangeA = 0;
                rangeB = school.getEntrance().size() - 1;
                System.out.println("Please select a student to move (use number from 0 to " + rangeB
                        + " starting counting from left to right and top to bottom)");
                previousStateBeforeAdvancedCardPlayed = msgHandler.getState();
                msgHandler.setState(MOVE_STUDENT_SEND_MESSAGE);
            }
            case MOVE_MOTHER_NATURE_START -> {
                rangeA = 1;
                rangeB = playerDeck.getDiscardedCard().getMaximumSteps();
                System.out.println("How many step would you like to move mother nature (use a number between 1 and " + rangeB + ")");
                previousStateBeforeAdvancedCardPlayed = msgHandler.getState();
                msgHandler.setState(MOVE_MOTHER_NATURE_SEND_MESSAGE);
            }
            case CHOOSE_CLOUD_CARD_START -> {
                rangeA = 0;
                rangeB = board.getTerrain().getCloudCards().size() - 1;
                System.out.println("Select a Cloud from where to pick student (use number from 0 to " + rangeB
                        + " to select the cloud):\n");
                previousStateBeforeAdvancedCardPlayed = msgHandler.getState();
                msgHandler.setState(CHOOSE_CLOUD_CARD_SEND_MESSAGE);
            }
        }
    }

    /**
     * Based on the FSM status, it asks the user to insert the required parameters to run the chosen command and then
     * sends the action chosen to the server.
     * @param selection
     */
    protected void actionStateMachine(int selection){
        boolean playable = true;
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        SchoolBoard school = board.getSchools().get(board.getNames().indexOf(client.getName()));
        List<IslandBoard> islands = board.getTerrain().getIslands();
        switch(msgHandler.getState()) {
            case PLAY_ADVANCED_CARD:
                // We use indexes to let the player select the card he wants to play.
                System.out.println("Select an advanced card (use a number from 0 to 2):");
                for(AdvancedCardBoard card : board.getTerrain().getAdvancedCard()) System.out.println(card);
                int selectedCard;

                do {
                    selectedCard = whileInputNotIntegerInRange(0, 2);
                    // If the player has not enough money he can't play the card, so we send him back to the previous state.
                    if (board.getTerrain().getAdvancedCard().get(selectedCard).getCost() > board.getMoneys().get(board.getNames().indexOf(client.getName()))) {
                        msgHandler.setState(previousStateBeforeAdvancedCardPlayed);
                        displayErrorMessage(NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD, NOT_ENOUGH_MONEY_FOR_ADVANCED_CARD_ERROR, board);
                        playable = false;
                        break;
                    }

                    // If the player has already played a character card this turn he cannot play another one, so we
                    // send him back to the previous state.
                    if(school.isAdvancedAlreadyPlayedThisTurn()) {
                        playable = false;
                        msgHandler.setState(previousStateBeforeAdvancedCardPlayed);
                        displayErrorMessage(ADVANCED_CARD_ALREADY_PLAYED_IN_TURN, ADVANCED_CARD_ALREADY_PLAYED_IN_TURN_ERROR, board);
                        break;
                    }
                } while (board.getTerrain().getAdvancedCard().get(selectedCard).getCost() > board.getMoneys().get(board.getNames().indexOf(client.getName())));

                // If the card is playable than we send the proper action to the server.
                // Otherwise, we don't do anything.
                if(playable) {
                    client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAdvancedCardAction(client.getName(), board.getTerrain().getAdvancedCard().get(selectedCard).getType(), new AdvancedCardInputHandler(board.getTerrain().getAdvancedCard().get(selectedCard).getType(), this).getCardInputs())));
                    Logger.INFO("You played: " + Printable.TEXT_YELLOW + board.getTerrain().getAdvancedCard().get(selectedCard).getType().toString() + Printable.TEXT_RESET);
                    msgHandler.setActionSent(true);
                }
                break;

            case SELECT_ASSISTANT_CARD_SEND_MESSAGE:
                // The assistants cards are picked by using an index of the array of the still available cards.
                System.out.println("You selected: " + playerDeck.getCards().get(selection).getType().getName());
                client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAssistantCardAction(client.getName(), selection)));
                msgHandler.setActionSent(true);
                break;

            case MOVE_STUDENT_SEND_MESSAGE:
                rangeA = 0;
                rangeB = islands.size();
                // If the player is moving a student then we make him choose the index of the island on which to move
                // the student on, or by selecting 'max_index+1' the student will be moved into the dining hall.
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
                // Sending the mother nature amount of steps to the server with the proper action.
                client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new MoveMotherNatureAction(client.getName(), selection)));
                msgHandler.setActionSent(true);
                break;

            case CHOOSE_CLOUD_CARD_SEND_MESSAGE:
                // If the picked cloud is empty we show an error message, otherwise the selection is sent to the server
                // using the proper action.
                if(board.getTerrain().getCloudCards().get(selection).isEmpty()){
                    displayErrorMessage(INVALID_CLOUD_CHOSEN, INVALID_CLOUD_CHOSEN_ERROR, board);
                }
                else {
                    client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new ChooseCloudCardAction(client.getName(), selection)));
                    msgHandler.setActionSent(true);
                }
        }
    }


    /**
     * Asks the user to insert an integer value in between the given range.
     * @param a lower bound of the range.
     * @param b upper bound of the range.
     * @return the value in the range picked by the user.
     */
    protected synchronized int whileInputNotIntegerInRange(int a, int b){
        String read;
        boolean first = true;
        do{
            if(!first) {
                System.out.println("Not a number or not in the range given");
            }
            else {
                first = false;
            }
            read = input.nextLine();
            // In expert mode games if the user types play then we offer the possibility to play an advanced card.
            if(gameStarted && board.isExpertMode() && read.equals("play") && board.getPhase().toString().startsWith("ACTION_PHASE")){
                msgHandler.setState(PLAY_ADVANCED_CARD);
                return 0;
            }
            // we continue reading until the user insert a valid digit in the specified range.
        }while (read.isEmpty() || !read.chars().allMatch(Character::isDigit) ||
                Integer.parseInt(read) < a || Integer.parseInt(read) > b);
        return Integer.parseInt(read);
    }

    /**
     * Asks the user to insert a string which has to be contained in the given <code>container</code>.
     * @param container the list of accepted words.
     * @return the word which is part of the container list, chosen by the user.
     */
    private synchronized String whileInputNotContainedIn(List<String> container){
        String read = input.nextLine();
        while(!container.contains(read)){
            System.out.println("Selection not available please follow instruction");
            read = input.nextLine();
        }
        return read;
    }

    /**
     * Returns a printable string representing the provided list of decks.
     * @param decks the list of available decks.
     * @return a printable string representing the provided list of decks.
     *
     * @see DeckType
     */
    private String decksToString(List<?> decks){
        String msg = "";
        for(int i = 0; i < decks.size(); i++){
            msg = msg.concat("\n" + i + " = " + ((DeckType)decks.get(i)).getName());
        }
        return msg;
    }

    /**
     * Returns the input scanner.
     * @return the input scanner.
     *
     * @see Scanner
     */
    public Scanner getInput() {
        return input;
    }

}
