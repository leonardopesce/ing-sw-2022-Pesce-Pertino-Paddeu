package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.Observer;

import java.util.Timer;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PING;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PONG;

public class ClientConnectionStatusHandler extends ConnectionStatusHandler implements Observer<CommunicationMessage> {
    private Client clientHandled;

    public ClientConnectionStatusHandler() {
        super();
    }

    public void setClient(Client client) {
        clientHandled = client;
    }

    @Override
    public void run() {
        while (connectionActive) {
            try {
                pingTimer.schedule(new PingTimeoutExceededTask(this), 2L*PING_TIMEOUT_DELAY);
                Thread.sleep(2L*PING_TIMEOUT_DELAY);
            } catch (InterruptedException sleepError) {
                Logger.ERROR("Connection handler failed to sleep...", sleepError.getMessage());
                abortConnection();
            } catch (IllegalStateException alreadyCanceled) {
                // If the schedule was already canceled it's fine.
            }
        }
    }

    public void kill() {
        connectionActive = false;
    }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void abortConnection() {
        Logger.ERROR( "Connection timed out with the server. You have been disconnected from the server and the application will now close.", "Ping time limit exceeded");
        kill();
        Thread.currentThread().interrupt();
        System.exit(-1);
    }

    @Override
    public void update(CommunicationMessage message) {
        if(message.getID() == PING) {
            //Logger.INFO(new Timestamp(new Date().getTime()) + " - Received ping from server");
            clientHandled.asyncWriteToSocket(new CommunicationMessage(PONG, null));
            pingTimer.cancel();
            pingTimer = new Timer();
        }
    }
}
