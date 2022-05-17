package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12347;
    private final ServerSocket serverSocket;
    private final List<ClientConnection> waitingConnection = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final List<Lobby> activeGames = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * given a client connection, remove all the players in the same lobby of the client connection, then close
     * the lobby
     * @param c client connection
     */
    public synchronized void deregisterConnection(ClientConnection c) {
        if(waitingConnection.contains(c)){
            waitingConnection.remove(c);
        }
        else {
            for(int i = 0; i < activeGames.size(); i++) {
                if (activeGames.get(i).getConnectedPlayersToLobby().contains(c)) {
                    activeGames.remove(i).closeLobby(c);
                }
            }
        }
    }

    /**
     * Manage a given lobby: if the number of players in the lobby is acceptable, the game starts.<br>
     * Remove the connection which joined the lobby from the server waining connection list.
     * @param lobbyToHandle
     * @param connectionToMove connection on the server waiting connection list
     */
    //Wait for other players
    public synchronized void handleLobbyState(Lobby lobbyToHandle, ClientConnection connectionToMove){
        waitingConnection.remove(connectionToMove);
        if(lobbyToHandle.isFull()) {
            // The game is startable
            new Thread(lobbyToHandle).start();
        }
    }

    /**
     * Run the server, create new server client connections.
     * @throws IOException
     */
    public void run() throws IOException {
        int connections = 0;
        boolean running = true;

        Logger.INFO("Server is running");

        while(running){
            try {
                Socket newSocket = serverSocket.accept();
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
     * Get each player nickname, whether the player is on the server waiting connection list, whether a player
     * is on a lobby.
     * @return a set of strings containing players nicknames
     */
    public synchronized Set<String> getConnectedPlayersName() {
        Set<String> currentlyPlayingNicknames = new HashSet<>();

        for (ClientConnection connection : waitingConnection) {
            currentlyPlayingNicknames.add(((SocketClientConnection)connection).getClientName());
        }

        for(Lobby gameLobby : activeGames) {
            for(ClientConnection player : gameLobby.getConnectedPlayersToLobby()) {
                currentlyPlayingNicknames.add(((SocketClientConnection)player).getClientName());
            }
        }

        return currentlyPlayingNicknames;
    }

    public synchronized void addGameLobby(Lobby newLobby) {
        activeGames.add(newLobby);
    }

    public synchronized List<Lobby> getActiveGames() {
        return activeGames;
    }

    public void newWaitingConnection(ClientConnection connection) throws IOException, ClassNotFoundException{
        synchronized (this) {
            waitingConnection.add(connection);
        }
    }
}
