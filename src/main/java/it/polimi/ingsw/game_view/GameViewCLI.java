package it.polimi.ingsw.game_view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.ChooseCloudCardAction;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_controller.action.PlayAssistantCardAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.board.DeckBoard;
import it.polimi.ingsw.game_view.board.GameBoard;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


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
        System.out.println(board.print());
        asyncReadInput();
    }

    @Override
    public void update(MoveMessage message) {

    }

    public void asyncReadInput(){
        new Thread(() -> {
            while(client.isActive()){
                switch (GameAction.gameActionID.values()[whileInputNotIntegerInRange(0, 10)]){
                    case PLAY_ASSISTANT_CARD_ACTION -> playAssistantCardAction();
                    case CHOOSE_CLOUD_CARD_ACTION -> chooseCloudCardAction();

                }
            }
        }).start();
    }


    private int whileInputNotIntegerInRange(int a, int b){
        String read = input.nextLine();
        while(!Pattern.compile(String.format("^[%d-%d]",a,b)).matcher(read).find()){
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

    @Override
    protected void showMessage(CommunicationMessage message) {
        if(message.getID() == GAME_ACTION){
            ((GameBoard)message.getMessage()).print();
        }
    }


    private void playAssistantCardAction(){
        DeckBoard playerDeck = board.getDecks().get(board.getNames().indexOf(client.getName()));
        int selectedCard;
        System.out.println("Select an assistant card to play (use value from 0 to " + (playerDeck.getCards().size() - 1)
                + " to select the card):\n");
        System.out.println(playerDeck.print());
        selectedCard = whileInputNotIntegerInRange(0, playerDeck.getCards().size() - 1);
        System.out.println("You selected: " + playerDeck.getCards().get(selectedCard).getName());
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new PlayAssistantCardAction(client.getName(), selectedCard)));
    }

    private void chooseCloudCardAction(){
        System.out.println("Select a Cloud from where to pick student (use number from 0 to" + (board.getTerrain().getCloudCards().size() - 1)
                + " to select the cloud):\n");
        int selectedCard = whileInputNotIntegerInRange(0, board.getTerrain().getCloudCards().size() - 1);
        while(board.getTerrain().getCloudCards().get(selectedCard).isEmpty()){
            System.out.println("You selected an empty cloud, please pick another one");
            selectedCard = whileInputNotIntegerInRange(0, board.getTerrain().getCloudCards().size() - 1);
        }
        client.asyncWriteToSocket(new CommunicationMessage(GAME_ACTION, new ChooseCloudCardAction(client.getName(), selectedCard)));
    }
}
