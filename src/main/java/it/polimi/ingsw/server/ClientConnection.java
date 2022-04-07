package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observer;

public interface ClientConnection {

    void closeConnection();

    void addObserver(Observer<Object> observer);

    void asyncSend(Object message);
}
