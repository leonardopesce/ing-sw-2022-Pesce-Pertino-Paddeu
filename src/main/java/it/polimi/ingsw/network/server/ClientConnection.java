package it.polimi.ingsw.network.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_view.RemoteGameView;
import it.polimi.ingsw.observer.Observer;

import java.net.Socket;
import java.net.SocketException;

/**
 * Interface of a ClientConnection server side.
 */
public interface ClientConnection {
    /**
     * Closes the socket connection server side.
     */
    void closeConnection();

    /**
     * Adds an observer.
     * @param messageReceiver the class which handles the incoming messages.
     */
    void addObserver(Observer<CommunicationMessage> messageReceiver);

    /**
     * Sends a message asynchronously.
     * @param message the message to be sent.
     */
    void asyncSend(CommunicationMessage message);

    /**
     * Returns whether the connection is active or not.
     * @return true if the connection is alive server side, false otherwise.
     */
    boolean isActive();
}
