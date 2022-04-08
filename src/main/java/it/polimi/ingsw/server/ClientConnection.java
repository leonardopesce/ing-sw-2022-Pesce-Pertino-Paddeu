package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.observer.Observer;

public interface ClientConnection {

    void closeConnection();

    void addObserver(Observer<Object> observer);

    void asyncSend(CommunicationMessage message);
}
