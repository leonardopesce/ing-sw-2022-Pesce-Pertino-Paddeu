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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    private final ClientConnection lobbyOwner;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final List<ClientConnection> connectedPlayersToLobby = new ArrayList<>();
    private final boolean expertMode;
    private final int numberOfPlayers;

    public Lobby(ClientConnection owner, int gameSize, boolean isExpertGame) {
        lobbyOwner = owner;
        connectedPlayersToLobby.add(owner);
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
    }

    @Override
    public void run() {
        Game game = isExpertMode() ? new GameExpertMode(numberOfPlayers) : new Game(numberOfPlayers);
        GameController controller = new GameController(game);

        for(ClientConnection connection : connectedPlayersToLobby){
            DeckType deck = ((SocketClientConnection)connection).askDeckType(controller.getAvailableDeckType());
            Player player = controller.createPlayer(((SocketClientConnection) connection).getClientName(), deck);

            RemoteGameView view = new RemoteGameView(player.getNickname(), connection);
            game.addObserver(view);
            view.addObserver(controller);
            executor.submit(((SocketClientConnection)connection));
        }

        for(ClientConnection connection : connectedPlayersToLobby) {
            ((SocketClientConnection) connection).send(new CommunicationMessage(CommunicationMessage.MessageType.GAME_READY, expertMode ? new GameBoardAdvanced(game) : new GameBoard(game)));
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
