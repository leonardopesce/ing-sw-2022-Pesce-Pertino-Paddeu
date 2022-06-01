package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the server.
 */
public class Server {
    private static final int PORT = 12347;
    private final ServerSocket serverSocket;
    /**
     * A list of connections which have been registered to the server but haven't joined yet any lobby.
     */
    private final List<ClientConnection> waitingConnection = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final List<Lobby> activeGames = new ArrayList<>();
    private final String[] bannedNicks = {"GO_BACK_TO_JOIN_ACTION"};

    /**
     * //@param serverPort the port to assign to the server, passed via terminal argument.
     * @throws IOException if an I/O  error occurs when the server is getting created.
     */
    public Server(int serverPort) throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
    }

    /**
     * Given a client connection, if it isn't already part of a lobby, then it simply gets removed from the waiting connections (a list of all those connections
     * which are not already part of a lobby). Otherwise, the lobby containing the given connection gets closed.
     * @param c the client connection to unregister from the server.
     */
    public synchronized void deregisterConnection(ClientConnection c) {
        // If the connection is not already part of a lobby we simply remove it from the waiting connections (a list of all those connections which are not already
        // part of a lobby).
        if(waitingConnection.contains(c)){
            waitingConnection.remove(c);
        }
        else {
            // Otherwise, the lobby containing the given connection gets closed.
            for(int i = 0; i < activeGames.size(); i++) {
                if (activeGames.get(i).getConnectedPlayersToLobby().contains(c)) {
                    activeGames.remove(i).closeLobby(c);
                }
            }
        }
    }

    /**
     * Manage a given lobby: if the number of players in the lobby is acceptable (the lobby is full), the game starts.<br>
     * Remove the connection which joined the lobby from the server 'waiting connections' list.
     * @param lobbyToHandle the lobby whose state has to be handled.
     * @param connectionToMove connection on the server waiting connection list which joined the given lobby.
     */
    public synchronized void handleLobbyState(Lobby lobbyToHandle, ClientConnection connectionToMove){
        // Removing the connection from the waiting ones.
        waitingConnection.remove(connectionToMove);
        // If the lobby is full the game can start.
        if(lobbyToHandle.isFull()) {
            new Thread(lobbyToHandle).start();
        }
    }

    /**
     * Run the server and create new server client connections when new clients connect.
     * @throws IOException if an I/O error occurs when closing the socket.
     */
    public void run() throws IOException {
        int connections = 0;
        boolean running = true;

        Logger.INFO("Server is running");

        while(running){
            try {
                // While the server is running it accepts new clients connections.
                Socket newSocket = serverSocket.accept();
                // Once a new player connects a SocketClientConnection object gets assigned to the connection, and it will handle its status.
                connections++;
                Logger.INFO("Ready for the new connection - " + connections);
                executor.submit(new SocketClientConnection(newSocket, this));
            } catch (IOException e) {
                running = false;
                serverSocket.close();
                Logger.ERROR("Connection Error!", e.getMessage());
            }
        }
    }

    /**
     * Get each player nickname, whether the player is on the server waiting connection list, or he
     * is in a lobby.
     * @return a set of strings containing players nicknames of the players which are connected to the server.
     */
    public synchronized Set<String> getConnectedPlayersName() {
        Set<String> currentlyPlayingNicknames = new HashSet<>();

        // Fetching the player nicknames of the players which are connected to the server but haven't joined a lobby yet.
        for (ClientConnection connection : waitingConnection) {
            currentlyPlayingNicknames.add(((SocketClientConnection)connection).getClientName());
        }

        // Fetching all players nicknames of all those players which are connected to the server and are part of a lobby.
        for(Lobby gameLobby : activeGames) {
            for(ClientConnection player : gameLobby.getConnectedPlayersToLobby()) {
                currentlyPlayingNicknames.add(((SocketClientConnection)player).getClientName());
            }
        }

        return currentlyPlayingNicknames;
    }

    /**
     * Adds to the list of lobbies a new lobby.
     * @param newLobby the new lobby which has to be added to the active ones.
     */
    public synchronized void addGameLobby(Lobby newLobby) {
        activeGames.add(newLobby);
    }

    /**
     * Returns the list of active lobbies currently open on the server.
     * @return the list of active lobbies currently open on the server.
     */
    public synchronized List<Lobby> getActiveGames() {
        return activeGames;
    }

    /**
     * Adds a brand new connection to the waiting connections.
     * @param connection the connection of the client which has just joined the server.
     */
    public void newWaitingConnection(ClientConnection connection) {
        synchronized (this) {
            waitingConnection.add(connection);
        }
    }

    /**
     * Returns the list of banned nicknames (no one can choose these nicknames as their name).
     * @return the list of banned nicknames.
     */
    public String[] getBannedNicks() {
        return bannedNicks;
    }
}
