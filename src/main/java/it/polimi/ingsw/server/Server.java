package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_view.RemoteGameView;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.GameBoardAdvanced;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.ERROR;
import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.GAME_READY;

public class Server {
    private static final int PORT = 12345;
    private final ServerSocket serverSocket;
    private final List<ClientConnection> waitingConnection = new ArrayList<>();
    private final List<Lobby> activeGames = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public List<ClientConnection> getWaitingConnection() {
        return waitingConnection;
    }

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c) {
        /*
        Optional<List<ClientConnection>> opponent = playingConnection.stream().reduce((list1, list2) -> list1.contains(c) ? list1 : list2);
        opponent.ifPresent(list -> {
            playingConnection.remove(list);
            list.forEach(ClientConnection::closeConnection);
        });

        waitingConnection.remove(c);
        */
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
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
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

        ((SocketClientConnection)connection).askJoiningAction();
    }
}
