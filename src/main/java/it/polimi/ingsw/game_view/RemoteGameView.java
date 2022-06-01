package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.GameBoardAdvanced;
import it.polimi.ingsw.network.server.SocketClientConnection;
import it.polimi.ingsw.network.utils.Logger;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.network.server.ClientConnection;

import java.util.Arrays;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

/**
 * Used by the server as a way to interface the communication coming and going to the client
 */
public class RemoteGameView extends Observable<GameAction> implements Observer<MoveMessage>{
    private final ClientConnection clientConnection;
    private final String playerName;

    /**
     * Constructor class initialize the variable used in the class
     * @param playerName String containing the name of the player
     * @param c Connection of the client with function to send message to it
     * @see ClientConnection
     */
    public RemoteGameView(String playerName, ClientConnection c) {
        this.playerName = playerName;
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
    }

    /**
     * A sub-Class used in combination with the Observer for receiving a message from the Client
     */
    private class MessageReceiver implements Observer<CommunicationMessage> {

        @Override
        public void update(CommunicationMessage message) {
            if(message.getID() != PONG) {
                Logger.GAME_LOG("Received: " + message.getID().toString(), ((SocketClientConnection)clientConnection).getClientName());
            }
            try{
                if(message.getID() == GAME_ACTION) {
                    handleMove((GameAction) message.getMessage());
                }
            }catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
                clientConnection.asyncSend(new CommunicationMessage(ERROR,"Error!"));
            }
        }

    }

    /**
     * Handles the move requested by the player
     * @param action the action requested by the player
     * @see GameAction
     */
    void handleMove(GameAction action) {
        Logger.GAME_LOG("from player: " + playerName + ", received game action: " + action.toString(), ((SocketClientConnection)clientConnection).getClientName());
        notify(action);
    }

    /**
     * Sends message to the client saved in this clientConnection
     * @param message the message to send
     */
    protected void sendMessage(CommunicationMessage message){
        clientConnection.asyncSend(message);
    }

    /**
     * Update function necessary for the observer to work when receiving a message from the Model analyze and send
     * it to the client
     * @param message the message received from the model
     */
    @Override
    public void update(MoveMessage message){
        boolean gameOver = message.getGame().winner().length != 0;
        boolean draw = message.getGame().MAX_PLAYERS == 4 ? (message.getGame().winner().length == 4) :(message.getGame().winner().length == 2 || message.getGame().winner().length == 3);
        if (gameOver) {
            sendMessage(new CommunicationMessage(draw ? DRAW :
                    (Arrays.asList(message.getGame().winner()).contains(playerName) ?
                            YOU_WIN : YOU_LOSE), message.isExpertMode() ? new GameBoardAdvanced(message.getGame()) : new GameBoard(message.getGame()))
            );
        }
        else {
            if(message.getType() == ASSISTANT_NOT_PLAYABLE) {
                if(message.getGame().getCurrentlyPlayingPlayer().getNickname().equals(((SocketClientConnection)clientConnection).getClientName())) sendMessage(new CommunicationMessage(message.getType(), message.isExpertMode() ? new GameBoardAdvanced(message.getGame()) : new GameBoard(message.getGame())));
            } else {
                sendMessage(new CommunicationMessage(message.getType(), message.isExpertMode() ? new GameBoardAdvanced(message.getGame()) : new GameBoard(message.getGame())));
            }
        }
    }

}
