package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final List<ClientConnection> waitingConnection = new ArrayList<>();
    private final List<Lobby> activeGames = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

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

    //Wait for other players
    public synchronized void handleLobbyState(Lobby lobbyToHandle){
        if(lobbyToHandle.isFull()) {
            // The game is startable
            new Thread(lobbyToHandle).start();
        }
    }

    public void run() throws IOException {
        int connections = 0;
        boolean running = true;
        System.out.println("Server is running");

        while(running){
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                new SocketClientConnection(newSocket, this);
            } catch (IOException e) {
                running = false;
                serverSocket.close();
                System.out.println("Connection Error!");
            }
        }
    }

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

    public void newWaitingConnection(ClientConnection connection) {
        synchronized (this) {
            waitingConnection.add(connection);
        }

        try {
            ((SocketClientConnection)connection).askJoiningAction();
        } catch (IOException | ClassNotFoundException e) {
            deregisterConnection(connection);
        }
    }
}
