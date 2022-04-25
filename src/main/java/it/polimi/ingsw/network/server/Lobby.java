package it.polimi.ingsw.network.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_view.RemoteGameView;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.GameBoardAdvanced;
import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.ERROR;

public class Lobby implements Runnable {
    private final Server server;
    private final ClientConnection lobbyOwner;
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

    public synchronized void registerClientToLobby(ClientConnection newClient) throws IOException{
        connectedPlayersToLobby.add(newClient);

        // Notify all the lobby participants that a new player has joined
        for(ClientConnection lobbyParticipant : connectedPlayersToLobby) {
            ((SocketClientConnection)lobbyParticipant).send(new CommunicationMessage(ERROR, getLastJoined() + " has joined the lobby."));
        }
    }

    public synchronized void closeLobby(ClientConnection connectionWhoMadeTheLobbyClose) {
        server.getActiveGames().remove(this);
        for(ClientConnection lobbyPartecipant : connectedPlayersToLobby) {
            if(!((SocketClientConnection)lobbyPartecipant).getClientName().equals(((SocketClientConnection)connectionWhoMadeTheLobbyClose).getClientName())) {
                try {
                    ((SocketClientConnection) lobbyPartecipant).send(new CommunicationMessage(ERROR, ((SocketClientConnection) connectionWhoMadeTheLobbyClose).getClientName() + "'s connection has been interrupted. The lobby will now close and you will be disconnected from the server."));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((SocketClientConnection) lobbyPartecipant).close();
            }
        }
        connectedPlayersToLobby.clear();
    }

    public synchronized boolean checkInactivityClients() {
        return connectedPlayersToLobby.stream().anyMatch(c -> !c.isActive());
    }

    @Override
    public void run() {
        Game game = isExpertMode() ? new GameExpertMode(numberOfPlayers) : new Game(numberOfPlayers);
        GameController controller = new GameController(game);

        for(ClientConnection connection : connectedPlayersToLobby) {
            try {
                ((SocketClientConnection) connection).send(new CommunicationMessage(ERROR, "The lobby is full:\n" + this + "The game is starting...\n"));
            } catch (IOException e) {
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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                closeLobby(connection);
            }
        }

        for(ClientConnection connection : connectedPlayersToLobby) {
            try {
                ((SocketClientConnection) connection).send(new CommunicationMessage(CommunicationMessage.MessageType.GAME_READY, expertMode ? new GameBoardAdvanced(game) : new GameBoard(game)));
            } catch (IOException e) {
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
