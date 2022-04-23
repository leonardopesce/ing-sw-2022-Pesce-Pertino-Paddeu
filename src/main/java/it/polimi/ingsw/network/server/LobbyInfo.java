package it.polimi.ingsw.network.server;

import it.polimi.ingsw.game_view.board.Printable;

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

    @Override
    public String toString(){
        return lobbyName + " | " + (isLobbyFull ? Printable.TEXT_RED + currentLobbySize + "/" + lobbyMaxSize
                + " Lobby Full" + Printable.TEXT_RESET : Printable.TEXT_GREEN + currentLobbySize + "/"
                + lobbyMaxSize + Printable.TEXT_RESET);
    }
}
