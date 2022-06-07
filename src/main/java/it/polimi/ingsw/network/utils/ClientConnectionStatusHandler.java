package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.observer.Observer;

import java.util.Timer;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PING;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PONG;

/**
 * A connection status handler used client-side.
 *
 * <p>
 *     It gets notified when a new <code>PING</code> message is received by the client and responds immediately with
 *     a <code>PONG</code>.
 * </p>
 *
 * <p>
 *     If the client does not receive a ping for more than <code>2*PING_TIMEOUT_DELAY</code> milliseconds, than a
 *     {@link PingTimeoutExceededTask} is run (the connection will be aborted).
 * </p>
 *
 * @see Client
 * @see PingTimeoutExceededTask
 * @see ConnectionStatusHandler
 */
public class ClientConnectionStatusHandler extends ConnectionStatusHandler implements Observer<CommunicationMessage> {
    private Client clientHandled;

    /**
     * When this object has been created, the client connection status is set to 'alive'.
     */
    public ClientConnectionStatusHandler() {
        super();
    }

    /**
     * Sets the client of which the connection is monitored by this object.
     * @param client the client of which the connection is monitored by this object.
     */
    public void setClient(Client client) {
        clientHandled = client;
    }

    /**
     * Runs the ClientConnectionStatusHandler. It will periodically schedule a {@link PingTimeoutExceededTask} which will
     * be removed when the client receives a ping from the server.
     *
     * @see Timer
     * @see PingTimeoutExceededTask
     */
    @Override
    public void run() {
        while (connectionActive) {
            try {
                // Every 2*PING_TIMEOUT_DELAY milliseconds a new Task is set up. It is deleted when the client receives
                // a PING message.
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

    @Override
    public void kill() {
        connectionActive = false;
        pingTimer.cancel();
    }

    @Override
    public boolean isConnectionActive() {
        return connectionActive;
    }

    @Override
    public void abortConnection() {
        Logger.ERROR( "Connection timed out with the server. You have been disconnected from the server and the application will now close.", "Ping time limit exceeded");
        Logger.INFO("\nThis error may be caused by:\n\t(1) The server internet connection is not available anymore, so it stopped pinging;\n\t(2) Your internet connection is no longer available, so you haven't received a ping during the last 20 seconds.\n Check your internet connection and the availability of the server.");
        kill();
        Thread.currentThread().interrupt();
        System.exit(-1);
    }

    /**
     * The object gets notified of ping messages. When a <code>PING</code> message is received the Client answers back
     * immediately with a <code>PONG</code> message and resets the timer, by cancelling the previous scheduled task.
     * @param message the message notified by the client.
     *
     * @see CommunicationMessage
     * @see Timer
     * @see PingTimeoutExceededTask
     */
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
