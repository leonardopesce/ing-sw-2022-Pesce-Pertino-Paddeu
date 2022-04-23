package it.polimi.ingsw.network.server;

import java.io.Serializable;

public class LobbyInfo implements Serializable {
    private static final long serialVersionUID = 3753L;

    private final String lobbyName;
    private final int currentLobbySize;
    private final int lobbyMaxSize;
    private final boolean isLobbyFull;

    public LobbyInfo(Lobby lobby) {
        lobbyName = lobby.getLobbyName();
        currentLobbySize = lobby.getCurrentLobbySize();
        lobbyMaxSize = lobby.getGameSize();
        isLobbyFull = lobby.isFull();
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getCurrentLobbySize() {
        return currentLobbySize;
    }

    public boolean isFull() {
        return isLobbyFull;
    }

    public int getLobbyMaxSize() {
        return lobbyMaxSize;
    }
}
