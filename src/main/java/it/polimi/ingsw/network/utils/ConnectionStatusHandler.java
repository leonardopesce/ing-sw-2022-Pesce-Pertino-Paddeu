package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.server.SocketClientConnection;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;
import java.util.Timer;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PING;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PONG;

public class ConnectionStatusHandler extends Thread implements Observer<CommunicationMessage> {
    private final int PING_TIMEOUT_DELAY = 10000;
    private final SocketClientConnection connection;
    private boolean connectionActive;
    private Timer pingTimer = new Timer();

    public ConnectionStatusHandler(SocketClientConnection connectionToHandle) {
        connection = connectionToHandle;
        connectionActive = true;
    }

    @Override
    public void run() {
        while (connectionActive) {
            try {
                connection.send(new CommunicationMessage(PING, null));
                pingTimer.schedule(new PingTimeoutExceededTask(this), PING_TIMEOUT_DELAY);
                Thread.sleep(10000);
            } catch (InterruptedException sleepError) {
                Logger.ERROR("Connection handler failed to sleep...", sleepError.getMessage());
                abortConnection();
            } catch(IOException sendError) {
                Logger.ERROR("Failed to send the ping message.", sendError.getMessage());
                abortConnection();
            }
        }
    }

    @Override
    public void update(CommunicationMessage message) {
        if(message.getID() == PONG) {
            pingTimer.cancel();
            pingTimer = new Timer();
        }
    }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void kill() {
        connectionActive = false;
    }

    public void abortConnection() {
        Logger.ERROR(connection.getClientName() + "'s connection aborted due to max ping limit exceeded.", "Ping time limit exceeded");
        Thread.currentThread().interrupt();
        connection.close();
        connectionActive = false;
    }
}
