package it.polimi.ingsw.network.utils;

import java.util.Timer;

public abstract class ConnectionStatusHandler extends Thread {
    protected final int PING_TIMEOUT_DELAY = 10000000;
    protected boolean connectionActive;
    protected Timer pingTimer = new Timer();

    public ConnectionStatusHandler() {
        connectionActive = true;
    }

    @Override
    public void run() {}

    public abstract void kill();
    public abstract boolean isConnectionActive();
    public abstract void abortConnection();
}
