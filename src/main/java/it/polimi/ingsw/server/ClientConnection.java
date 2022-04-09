package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;

public interface ClientConnection {

    void closeConnection();

    void asyncSend(CommunicationMessage message);
}
