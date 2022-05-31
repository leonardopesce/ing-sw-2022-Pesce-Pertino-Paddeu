package it.polimi.ingsw.network.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_view.RemoteGameView;
import it.polimi.ingsw.game_view.board.GameBoard;
import it.polimi.ingsw.game_view.board.GameBoardAdvanced;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

/**
 * Models a game lobby.
 */
public class Lobby implements Runnable {
    private final Server server;
    private final ClientConnection lobbyOwner;
    private final List<ClientConnection> connectedPlayersToLobby = new ArrayList<>();
    private final boolean expertMode;
    private final int numberOfPlayers;

    /**
     * @param server the server object in which the lobby is created.
     * @param owner the player's connection which created the lobby.
     * @param gameSize the size of the game handled by the lobby.
     * @param isExpertGame true if the game handled by the lobby has to be played in expert mode, false otherwise.
     */
    public Lobby(Server server, ClientConnection owner, int gameSize, boolean isExpertGame) {
        this.server = server;
        lobbyOwner = owner;
        expertMode = isExpertGame;
        numberOfPlayers = gameSize;
    }

    /**
     * Returns the size of the game handled by the lobby.
     * @return the size of the game handled by the lobby.
     */
    public synchronized int getGameSize() {
        return numberOfPlayers;
    }

    /**
     * Returns the list of {@link ClientConnection} of the players connected to the lobby.
     * @return the list of {@link ClientConnection} of the players connected to the lobby.
     */
    public synchronized List<ClientConnection> getConnectedPlayersToLobby() {
        return connectedPlayersToLobby;
    }

    /**
     * Returns how many players are connected to the lobby when the method is called.
     *
     * <p>E.g. : if the maximum lobby size is 3 and there are 2 players connected which are waiting for the third one, a call to this method will return 2.</p>
     * @return how many players are connected to the lobby when the method is called.
     */
    public synchronized int getCurrentLobbySize() {
        return connectedPlayersToLobby.size();
    }

    /**
     * Return whether the lobby is full or not.
     * @return true if the lobby is full, otherwise false.
     */
    public synchronized boolean isFull() {
        return getGameSize() == getCurrentLobbySize();
    }

    /**
     * Returns whether the game handled by the lobby is played in expert mode or not.
     * @return true if the game handled by the lobby is played in expert mode, otherwise false.
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Returns the name of the lobby.
     *
     * <p>With our implementation, the lobby name will always correspond to the nickname of the lobby owner.</p>
     * @return the name of the lobby.
     */
    public String getLobbyName() {
        return ((SocketClientConnection)lobbyOwner).getClientName();
    }

    /**
     * Returns the last client connection which has joined the lobby.
     * @return the last client connection which has joined the lobby.
     */
    public String getLastJoined() { return ((SocketClientConnection)connectedPlayersToLobby.get(connectedPlayersToLobby.size()-1)).getClientName(); }

    /**
     * Insert a new client into the lobby participants List.
     *
     * <p>
     *     This method is called when a new player wants to register to the lobby. After the insertion is successfully completed, the method notifies all the others
     *     participants the new client connected.
     * </p>
     * @param newClient the client connection of the player who wants to join the lobby.
     * @throws IOException if it is not possible to send the message to the clients already connected to the lobby.
     */
    public synchronized void registerClientToLobby(ClientConnection newClient) throws IOException{
        connectedPlayersToLobby.add(newClient);

        // Notify all the lobby participants that a new player has joined
        broadcastMessage(LOBBY_JOINED_CONFIRMED, new LobbyInfo(this), List.of(newClient));
    }

    /**
     * Close the lobby if a player disconnects.
     * @param connectionWhoMadeTheLobbyClose client who has disconnected and made the lobby close.
     */
    public synchronized void closeLobby(ClientConnection connectionWhoMadeTheLobbyClose) {
        // Removing the current lobby from the active lobbies in the server instance.
        server.getActiveGames().remove(this);

        // Every player, except the one who made the lobby close, gets notified that a client has disconnected and then that the lobby will close by also
        // disconnecting them from the lobby and server.
        for(ClientConnection lobbyPartecipant : connectedPlayersToLobby) {
            if(!((SocketClientConnection)lobbyPartecipant).getClientName().equals(((SocketClientConnection)connectionWhoMadeTheLobbyClose).getClientName())) {
                try {
                    ((SocketClientConnection) lobbyPartecipant).send(new CommunicationMessage(PLAYER_DISCONNECTED, ((SocketClientConnection) connectionWhoMadeTheLobbyClose).getClientName()));

                } catch (IOException e) {
                    Logger.ERROR("Error while sending the close lobby message to the lobby partecipants. Closing the lobby...", e.getMessage());
                }
                ((SocketClientConnection) lobbyPartecipant).close();
            }
        }
        connectedPlayersToLobby.clear();
    }

    /**
     * Send a message to all the client except the excluded ones.
     * @param messageType the type/id of the message which has to be broadcast.
     * @param messageContent the content of the message which has to be sent.
     * @param excludedClients a list of excluded clients. The message will not be sent to these clients.
     */
    private synchronized void broadcastMessage(CommunicationMessage.MessageType messageType, Object messageContent, List<ClientConnection> excludedClients) {
        for(ClientConnection connection : connectedPlayersToLobby) {
            // Checking if there are clients to exclude from the broadcast.
            if(excludedClients == null || !excludedClients.contains(connection)) {
                try {
                    ((SocketClientConnection)connection).send(new CommunicationMessage(messageType, messageContent));
                } catch (IOException e) {
                    Logger.ERROR("Error while broadcasting a message (" + messageType.toString() +") in the lobby.", e.getMessage());
                    closeLobby(connection);
                }
            }
        }
    }

    /**
     * When the lobby is full, this method makes the game start.
     */
    @Override
    public void run() {
        // An instance of the game and game controller is created based on the number of players and if the game must be in expert mode or not.
        Game game = isExpertMode() ? new GameExpertMode(numberOfPlayers) : new Game(numberOfPlayers);
        GameController controller = new GameController(game);

        // Notifying all the lobby participants that the lobby is full and that the game is starting
        broadcastMessage(INFO, "The lobby is full: \n" + this + "The game is starting...\n", null);

        for(ClientConnection connection : connectedPlayersToLobby){
            try {
                // Making the players choosing their deck type one by one. When player X is choosing the deck type, players Y,Z,W are notified
                // of that (and vice-versa).
                broadcastMessage(IS_CHOSING_DECK_TYPE, ((SocketClientConnection)connection).getClientName(), new ArrayList<>(List.of(connection)));

                // After the player has chosen his deck type, his corresponding player Object server side is created with the corresponding RemoteGameView.
                Player player = controller.createPlayer(
                        ((SocketClientConnection) connection).getClientName(),
                        ((SocketClientConnection) connection).askDeckType(controller.getAvailableDeckType()));

                RemoteGameView view = new RemoteGameView(player.getNickname(), connection);
                game.addObserver(view);
                view.addObserver(controller);
            } catch (IOException | ClassNotFoundException e) {
                Logger.ERROR("A problemwas found while starting the lobby. It will now close.", e.getMessage());
                closeLobby(connection);
            }
        }

        // When all the players are successfully set up, all the clients get notified that the game is ready to be played, sending the board.
        broadcastMessage(GAME_READY, expertMode ? new GameBoardAdvanced(game) : new GameBoard(game), null);
    }

    /**
     * Returns the string to be printed containing all the lobby information.
     * @return the string to be printed containing all the lobby information.
     */
    @Override
    public String toString() {
        StringBuilder lobbyContent = new StringBuilder("Lobby members: \n");
        for(ClientConnection connection : connectedPlayersToLobby) {
            lobbyContent.append(((SocketClientConnection) connection).getClientName()).append("\n");
        }

        return lobbyContent.toString();
    }
}
