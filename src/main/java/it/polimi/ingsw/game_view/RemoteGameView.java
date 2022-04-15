package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;

import java.util.Arrays;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class RemoteGameView extends Observable<GameAction> implements Observer<MoveMessage>{
    private final ClientConnection clientConnection;
    private final String playerName;

    public RemoteGameView(String playerName, ClientConnection c) {
        this.playerName = playerName;
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
    }

    private class MessageReceiver implements Observer<CommunicationMessage> {

        @Override
        public void update(CommunicationMessage message) {
            System.out.println("Received: " + message);
            try{
                if(message.getID() == GAME_ACTION) {
                    handleMove((GameAction) message.getMessage());
                }
            }catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
                clientConnection.asyncSend(new CommunicationMessage(ERROR,"Error!"));
            }
        }

    }

    void handleMove(GameAction action) {
        System.out.println("from player: " + playerName + ", received game action: " + action.toString());
        notify(action);
    }

    protected void sendMessage(CommunicationMessage message){
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(MoveMessage message){
        boolean gameOver = message.getGame().winner().length != 0;
        boolean draw = message.getGame().MAX_PLAYERS == 4 ? (message.getGame().winner().length == 4) :(message.getGame().winner().length == 2 || message.getGame().winner().length == 3);
        if (gameOver) {
            sendMessage(new CommunicationMessage(
                    Arrays.asList(message.getGame().winner()).contains(playerName) ?
                            YOU_WIN : YOU_LOSE, null)
            );
        }
        else {
            sendMessage(new CommunicationMessage(message.getType(), new GameBoard(message.getGame())));
        }
    }

}
