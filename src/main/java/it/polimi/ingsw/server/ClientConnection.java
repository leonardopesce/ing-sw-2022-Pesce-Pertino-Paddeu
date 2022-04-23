package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.RemoteGameView;
import it.polimi.ingsw.observer.Observer;

import java.net.Socket;
import java.net.SocketException;

public interface ClientConnection {

    void closeConnection();

    void addObserver(Observer<CommunicationMessage> messageReceiver);

    void asyncSend(CommunicationMessage message);

    boolean isActive();
}
