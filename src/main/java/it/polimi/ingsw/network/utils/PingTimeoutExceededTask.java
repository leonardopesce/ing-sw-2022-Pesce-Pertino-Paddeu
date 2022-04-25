package it.polimi.ingsw.network.utils;

import java.util.TimerTask;

public class PingTimeoutExceededTask extends TimerTask {
    private ConnectionStatusHandler connectionStatusHandler;

    public PingTimeoutExceededTask(ConnectionStatusHandler connectionStatusHandler) {
        this.connectionStatusHandler = connectionStatusHandler;
    }

    @Override
    public void run() {
        connectionStatusHandler.abortConnection();
    }
}
