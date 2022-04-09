package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.ERROR;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.GAME_ACTION;

public class RemoteGameView extends GameView {

    private ClientConnection clientConnection;

    private class MessageReceiver implements Observer<CommunicationMessage> {

        @Override
        public void update(CommunicationMessage message) {
            System.out.println("Received: " + message);
            try{
                if(message.getID() == GAME_ACTION){
                    handleMove((GameAction)message.getMessage());
                }

            }catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
                clientConnection.asyncSend(new CommunicationMessage(ERROR,"Error!"));
            }
        }

    }

    @Override
    protected void showMessage(Object message){
        //TODO send message to client
    }

    public RemoteGameView(ClientConnection c) {
        super();
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
    }

    @Override
    public void update(MoveMessage message){
        //showMessage(message.getGame());
        String resultMsg = "";
        boolean gameOver = message.getGame().winner().length == 0;
        boolean draw = message.getGame().MAX_PLAYERS == 4 ? (message.getGame().winner().length == 4) :(message.getGame().winner().length == 2 || message.getGame().winner().length == 3);
        if (gameOver) {
            /*if (message.getPlayer() == getPlayer()) {
                resultMsg = gameMessage.winMessage + "\n";
            } else {
                resultMsg = gameMessage.loseMessage + "\n";
            }*/
        }
        else {
            if (draw) {
            //    resultMsg = gameMessage.drawMessage + "\n";
            }
        }
        /*if(message.getPlayer() == getPlayer()){
            resultMsg += gameMessage.waitMessage;
        }
        else{
            resultMsg += gameMessage.moveMessage;
        }*/

        //showMessage(resultMsg);
    }

}
