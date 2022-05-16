package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.network.server.Lobby;

import it.polimi.ingsw.game_view.board.Printable;
import it.polimi.ingsw.network.server.SocketClientConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LobbyInfo implements Serializable {
    private static final long serialVersionUID = 3753L;

    private final String lobbyName;
    private final int currentLobbySize;
    private final int lobbyMaxSize;
    private final boolean isLobbyFull;
    private final boolean isLobbyExpert;
    private final List<String> lobbyMembers;

    public LobbyInfo(Lobby lobby) {
        lobbyName = lobby.getLobbyName();
        currentLobbySize = lobby.getCurrentLobbySize();
        lobbyMaxSize = lobby.getGameSize();
        isLobbyFull = lobby.isFull();
        isLobbyExpert = lobby.isExpertMode();
        lobbyMembers = new ArrayList<>();
        lobbyMembers.addAll(lobby.getConnectedPlayersToLobby().stream().map(clientConnection -> ((SocketClientConnection) clientConnection).getClientName()).toList());
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

    public boolean isLobbyExpert() {
        return isLobbyExpert;
    }

    public List<String> getLobbyMembers() {
        return lobbyMembers;
    }

    @Override
    public String toString(){
        return lobbyName + " | " + (isLobbyFull ? Printable.TEXT_RED + currentLobbySize + "/" + lobbyMaxSize
                + " Lobby Full" + Printable.TEXT_RESET : Printable.TEXT_GREEN + currentLobbySize + "/"
                + lobbyMaxSize + Printable.TEXT_RESET);
    }
}
