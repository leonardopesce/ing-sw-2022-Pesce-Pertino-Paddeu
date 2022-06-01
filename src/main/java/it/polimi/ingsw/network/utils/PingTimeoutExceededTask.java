package it.polimi.ingsw.network.utils;

import java.util.TimerTask;

/**
 * A task to be scheduled while waiting for a PING/PONG message respectively client and server side.
 * When the timeout is exceeded this task run and abort the current connection.
 */
public class PingTimeoutExceededTask extends TimerTask {
    private final ConnectionStatusHandler connectionStatusHandler;

    /**
     * @param connectionStatusHandler the connection status handler on which to perform the <code>abortConnection</code>
     *                                method.
     */
    public PingTimeoutExceededTask(ConnectionStatusHandler connectionStatusHandler) {
        this.connectionStatusHandler = connectionStatusHandler;
    }

    /**
     * Runs the <code>PingTimeoutExceededTask</code> then the timeout is exceeded and aborts the connection.
     */
    @Override
    public void run() {
        connectionStatusHandler.abortConnection();
    }
}
