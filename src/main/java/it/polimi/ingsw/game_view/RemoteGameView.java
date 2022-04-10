package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;

import java.util.Arrays;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class RemoteGameView extends GameView {
    private final ClientConnection clientConnection;

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

    @Override
    protected void showMessage(CommunicationMessage message){
        clientConnection.asyncSend(message);
    }

    public RemoteGameView(Player player, ClientConnection c) {
        super(player.getNickname());
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
    }

    @Override
    public void update(MoveMessage message){
        boolean gameOver = message.getGame().winner().length != 0;
        boolean draw = message.getGame().MAX_PLAYERS == 4 ? (message.getGame().winner().length == 4) :(message.getGame().winner().length == 2 || message.getGame().winner().length == 3);
        if(message.hasError()){
            showMessage(new CommunicationMessage(ERROR, message.getErrorMessage()));
        }
        if (gameOver) {
            showMessage(new CommunicationMessage(
                    Arrays.stream(message.getGame().winner()).anyMatch(str -> str.equals(getPlayer())) ?
                            YOU_WIN : YOU_LOSE, null)
            );
        }

        showMessage(new CommunicationMessage(VIEW_UPDATE, new GameBoard(message.getGame())));
    }

}
