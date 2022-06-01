package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.network.server.Lobby;

import it.polimi.ingsw.game_view.board.Printable;
import it.polimi.ingsw.network.server.SocketClientConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A serializable version of the lobby. It contains all the useful information about a specific lobby, like it's name,
 * how many players are connected at a specific time, its max size, whether it's full or not, whether the game handled
 * by the lobby is played in expert mode or not and all the members connected to the lobby.
 */
public class LobbyInfo implements Serializable {
    private static final long serialVersionUID = 3753L;

    private final String lobbyName;
    private final int currentLobbySize;
    private final int lobbyMaxSize;
    private final boolean isLobbyFull;
    private final boolean isLobbyExpert;
    private final List<String> lobbyMembers;

    /**
     * @param lobby the lobby of which to store the information.
     */
    public LobbyInfo(Lobby lobby) {
        lobbyName = lobby.getLobbyName();
        currentLobbySize = lobby.getCurrentLobbySize();
        lobbyMaxSize = lobby.getGameSize();
        isLobbyFull = lobby.isFull();
        isLobbyExpert = lobby.isExpertMode();
        lobbyMembers = new ArrayList<>();
        lobbyMembers.addAll(lobby.getConnectedPlayersToLobby().stream().map(clientConnection -> ((SocketClientConnection) clientConnection).getClientName()).toList());
    }

    /**
     * Returns the lobby name (equals the lobby owner's name).
     * @return the lobby name.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Returns how many players are connected to the lobby when the object is created.
     * @return how many players are connected to the lobby when the object is created.
     */
    public int getCurrentLobbySize() {
        return currentLobbySize;
    }

    /**
     * Returns whether the lobby is full or not.
     * @return true if the lobby is full (a.k.a. <code>currentLobbySize == lobbyMaxSize</code>.
     */
    public boolean isFull() {
        return isLobbyFull;
    }

    /**
     * Returns the lobby max size.
     * @return the lobby max size.
     */
    public int getLobbyMaxSize() {
        return lobbyMaxSize;
    }

    /**
     * Returns whether the game handled by the lobby is played in expert mode or not.
     * @return true if the game handled by the lobby is played in expert mode, otherwise false.
     */
    public boolean isLobbyExpert() {
        return isLobbyExpert;
    }

    /**
     * Returns a list of the nicknames of the players which are connected to the lobby when the LobbyInfo object is created.
     * @return a list of the nicknames of the players which are connected to the lobby when the LobbyInfo object is created.
     */
    public List<String> getLobbyMembers() {
        return lobbyMembers;
    }

    /**
     * Returns a formatted string to print out on the console the lobby information.
     * @return a formatted string to print out on the console the lobby information.
     */
    @Override
    public String toString(){
        return lobbyName + " | " + (isLobbyFull ? Printable.TEXT_RED + currentLobbySize + "/" + lobbyMaxSize
                + " Lobby Full" + Printable.TEXT_RESET : Printable.TEXT_GREEN + currentLobbySize + "/"
                + lobbyMaxSize + Printable.TEXT_RESET);
    }
}
