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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.ERROR;

public class Lobby implements Runnable {
    private final Server server;
    private final ClientConnection lobbyOwner;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final List<ClientConnection> connectedPlayersToLobby = new ArrayList<>();
    private final boolean expertMode;
    private final int numberOfPlayers;

    public Lobby(Server server, ClientConnection owner, int gameSize, boolean isExpertGame) {
        this.server = server;
        lobbyOwner = owner;
        expertMode = isExpertGame;
        numberOfPlayers = gameSize;
    }

    public synchronized int getGameSize() {
        return numberOfPlayers;
    }

    public synchronized List<ClientConnection> getConnectedPlayersToLobby() {
        return connectedPlayersToLobby;
    }

    public synchronized int getCurrentLobbySize() {
        return connectedPlayersToLobby.size();
    }

    public synchronized boolean isFull() {
        return getGameSize() == getCurrentLobbySize();
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public String getLobbyName() {
        return ((SocketClientConnection)lobbyOwner).getClientName();
    }

    public String getLastJoined() { return ((SocketClientConnection)connectedPlayersToLobby.get(connectedPlayersToLobby.size()-1)).getClientName(); }

    public synchronized void registerClientToLobby(ClientConnection newClient) {
        connectedPlayersToLobby.add(newClient);

        // Notify all the lobby participants that a new player has joined
        for(ClientConnection lobbyParticipant : connectedPlayersToLobby) {
            try {
                ((SocketClientConnection)lobbyParticipant).send(new CommunicationMessage(ERROR, getLastJoined() + " has joined the lobby."));
            } catch (SocketException e) {
                e.printStackTrace();
                closeLobby(lobbyParticipant);
            }
        }
    }

    public synchronized void closeLobby(ClientConnection connectionWhoMadeTheLobbyClose) {
        for(ClientConnection lobbyPartecipant : connectedPlayersToLobby) {
            if(!((SocketClientConnection)lobbyPartecipant).getClientName().equals(((SocketClientConnection)connectionWhoMadeTheLobbyClose).getClientName())) {
                try {
                    ((SocketClientConnection) lobbyPartecipant).send(new CommunicationMessage(ERROR, ((SocketClientConnection) connectionWhoMadeTheLobbyClose).getClientName() + "'s connection has been interrupted. The lobby will now close and you will be disconnected from the server."));
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                lobbyPartecipant.closeConnection();
            }
            server.deregisterConnection(lobbyPartecipant);
        }

        connectedPlayersToLobby.clear();
    }

    @Override
    public void run() {
        Game game = isExpertMode() ? new GameExpertMode(numberOfPlayers) : new Game(numberOfPlayers);
        GameController controller = new GameController(game);
        
        for(ClientConnection connection : connectedPlayersToLobby) {
            try {
                ((SocketClientConnection) connection).send(new CommunicationMessage(ERROR, "The lobby is full:\n" + this + "The game is starting...\n"));
            } catch (SocketException e) {
                e.printStackTrace();
                closeLobby(connection);
            }
        }

        for(ClientConnection connection : connectedPlayersToLobby){
            try {
                Player player = controller.createPlayer(
                        ((SocketClientConnection) connection).getClientName(),
                        ((SocketClientConnection) connection).askDeckType(controller.getAvailableDeckType()));

                RemoteGameView view = new RemoteGameView(player.getNickname(), connection);
                game.addObserver(view);
                view.addObserver(controller);
                executor.submit(((SocketClientConnection) connection));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                closeLobby(connection);
            }
        }

        for(ClientConnection connection : connectedPlayersToLobby) {
            try {
                ((SocketClientConnection) connection).send(new CommunicationMessage(CommunicationMessage.MessageType.GAME_READY, expertMode ? new GameBoardAdvanced(game) : new GameBoard(game)));
            } catch (SocketException e) {
                e.printStackTrace();
                closeLobby(connection);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder lobbyContent = new StringBuilder("Lobby members: \n");
        for(ClientConnection connection : connectedPlayersToLobby) {
            lobbyContent.append(((SocketClientConnection) connection).getClientName()).append("\n");
        }

        return lobbyContent.toString();
    }
}
