package it.polimi.ingsw.network.utils;

import java.util.Timer;

/**
 * Abstract class of an object which handles the status of the connection (rage-quits, ping failures, casual network
 * errors included).
 */
public abstract class ConnectionStatusHandler extends Thread {
    /**
     * The timeout delay to wait between sending a ping and receiving a pong (vice-versa for the client) before running
     * a {@link PingTimeoutExceededTask} which will close the connection.
     */
    protected final int PING_TIMEOUT_DELAY = 10000;
    protected boolean connectionActive;
    protected Timer pingTimer = new Timer();

    /**
     * When the object is created, the connection status is set to 'alive'.
     */
    public ConnectionStatusHandler() {
        connectionActive = true;
    }

    @Override
    public void run() {}

    /**
     * Kills the connection by setting false the attribute <code>connectionActive</code>
     *
     * <p>If <code>connectionActive</code> is false, then a call to <code>isConnectionActive</code> (always called by
     * the client and server before reading or sending a new message) will return false, so the client/server will be closed.</p>
     */
    public abstract void kill();

    /**
     * Returns whether the connection is still alive or not.
     * @return true if the connection is alive, false otherwise.
     */
    public abstract boolean isConnectionActive();

    /**
     * Effectively closes the connection with the counterpart when the timeout is exceeded.
     */
    public abstract void abortConnection();
}
