package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.server.SocketClientConnection;
import it.polimi.ingsw.observer.Observer;

import java.io.IOException;
import java.util.Timer;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PING;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.PONG;

/**
 * A connection status handler used server-side associated to each {@link SocketClientConnection}.
 *
 * <p>
 *     When this object is created, it starts pinging the counterpart every <code>PING_TIMEOUT_DELAY</code> milliseconds,
 *     with a <code>PING</code> message.
 * </p>
 *
 * <p>
 *     If the server does not receive a pong for more than <code>2*PING_TIMEOUT_DELAY</code> milliseconds, than a
 *     {@link PingTimeoutExceededTask} is run (the connection will be aborted).
 * </p>
 *
 * @see PingTimeoutExceededTask
 * @see ConnectionStatusHandler
 * @see SocketClientConnection
 */
public class ServerConnectionStatusHandler extends ConnectionStatusHandler implements Observer<CommunicationMessage> {
    private SocketClientConnection connection;

    /**
     * When this object has been created, the server connection, represented by the SocketClientConnection, status is
     * set to 'alive'.
     */
    public ServerConnectionStatusHandler() {
        super();
    }

    /**
     * Sets the connection of the player server-side which has to be handled by this object.
     * @param connectionToHandle the connection of the player server-side which has to be handled by this object.
     */
    public void setConnection(SocketClientConnection connectionToHandle) {
        connection = connectionToHandle;
    }

    /**
     * Until the connection with the client is alive, it periodically sends <code>PING</code> messages every
     * <code>PING_TIMEOUT_DELAY</code> milliseconds. For each ping, the client has <code>2*PING_TIMEOUT_DELAY</code>
     * milliseconds to answer back with a <code>PONG</code>
     *
     * <p>
     *     If the timeout exceed, the connection gets aborted server-side.
     * </p>
     */
    @Override
    public void run() {
        while (connectionActive) {
            try {
                // Sending a ping every PING_TIMEOUT_DELAY milliseconds.
                // Foreach ping the client has 2*PING_TIMEOUT_DELAY milliseconds to answer back with a PONG.
                connection.send(new CommunicationMessage(PING, null));
                pingTimer.schedule(new PingTimeoutExceededTask(this), 2L*PING_TIMEOUT_DELAY);
                Thread.sleep(PING_TIMEOUT_DELAY);
            } catch (InterruptedException sleepError) {
                Logger.ERROR("Connection handler failed to sleep...", sleepError.getMessage());
                abortConnection();
            } catch(IOException sendError) {
                Logger.ERROR("Failed to send the ping message.", sendError.getMessage());
                abortConnection();
            } catch (IllegalStateException alreadyCanceled) {
                // If the schedule was already been canceled, everything it's ok.
            }
        }
    }

    @Override
    public boolean isConnectionActive() {
        return connectionActive;
    }

    @Override
    public void abortConnection() {
        Logger.ERROR(connection.getClientName() + "'s connection aborted due to max ping limit exceeded.", "Ping time limit exceeded");
        Thread.currentThread().interrupt();
        connection.close();
        kill();
    }

    @Override
    public void kill() {
        connectionActive = false;
    }

    /**
     * The object gets notified of pong messages. When a <code>PONG</code> message is received the Server resets the timer,
     * by cancelling the previous scheduled task.
     * @param message the message notified.
     *
     * @see CommunicationMessage
     * @see Timer
     * @see PingTimeoutExceededTask
     */
    @Override
    public void update(CommunicationMessage message) {
        if(message.getID() == PONG) {
            //Logger.INFO(new Timestamp(new Date().getTime()) + " - Received pong from " + connection.getClientName());
            pingTimer.cancel();
            pingTimer = new Timer();
        }
    }
}
