package it.polimi.ingsw.network.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.network.utils.ConnectionStatusHandler;
import it.polimi.ingsw.network.utils.LobbyInfo;
import it.polimi.ingsw.network.utils.Logger;
import it.polimi.ingsw.network.utils.ServerConnectionStatusHandler;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

/**
 * class to manage connection between client and server
 */
public class SocketClientConnection extends Observable<CommunicationMessage> implements ClientConnection, Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;
    private String clientName;
    private final ServerConnectionStatusHandler connectionStatusHandler;
    private final LinkedList<CommunicationMessage> incomingMessages = new LinkedList<>();


    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Lobby> lobbies = server.getActiveGames().stream().filter(lobby -> lobby.getConnectedPlayersToLobby().stream().anyMatch(player -> !player.isActive())).toList();
        for (Lobby lobby : lobbies) {
            lobby.closeLobby(lobby.getConnectedPlayersToLobby().stream().filter(connection -> !connection.isActive()).toList().get(0));
        }

        new Thread(() -> {
            try {
                askName();
            } catch (Exception e) {
                Logger.ERROR("A problem was found during the player setup. Connection aborted.", e.getMessage());
                close();
            }
        }).start();

        this.connectionStatusHandler = new ServerConnectionStatusHandler();
        this.addObserver(connectionStatusHandler);
        connectionStatusHandler.setConnection(this);
        connectionStatusHandler.start();
    }

    public synchronized boolean isActive() {
        return connectionStatusHandler.isConnectionActive();
    }

    public synchronized void send(CommunicationMessage message) throws IOException {
        out.reset();
        out.writeObject(message);
        out.flush();
    }

    @Override
    public synchronized void closeConnection() {
        try {
            send(new CommunicationMessage(ERROR, "Connection closed"));
        } catch (IOException e) {
            Logger.WARNING("Cannot send a message to " + clientName + " because his socket is already closed maybe due to a disconnection.");
        }

        try {
            socket.close();
        } catch (IOException e) {
            Logger.ERROR("Error when closing socket!", e.getMessage());
        }
    }

    public void close() {
        if(isActive()) {
            connectionStatusHandler.kill();
            closeConnection();
            Logger.INFO("Unregistering " + clientName + "'s connection...");
            server.deregisterConnection(this);
            Logger.INFO(clientName + " has successfully been unregistered.");
        }
    }

    @Override
    public void asyncSend(final CommunicationMessage message) {
        new Thread(() -> {
            try {
                send(message);
            } catch (IOException e) {
                Logger.ERROR("Failed to async send the message..." + clientName + " will now be closed.", e.getMessage());
                close();
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            while (isActive()) {
                CommunicationMessage message = (CommunicationMessage) in.readObject();
                notify(message);
                if(message.getID() != PONG) {
                    synchronized (incomingMessages) {
                        incomingMessages.add(message);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.ERROR(clientName + " connection with the remote host has been interrupted.", e.getMessage());
            // e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * Server ask name to client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void askName() throws IOException, ClassNotFoundException {
        CommunicationMessage messageReceived = getResponse().get();
        String name = messageReceived.getMessage().toString();
        while ((messageReceived.getID() != NAME_MESSAGE) || server.getConnectedPlayersName().contains(name)) {
            send(new CommunicationMessage(NAME_MESSAGE, null));
            name = getResponse().get().getMessage().toString();
        }

        send(new CommunicationMessage(NAME_CONFIRMED, null));
        clientName = name;
        server.newWaitingConnection(this);
        askJoiningAction();
    }

    /**
     * Ask player if they want to create a game or join an existing one, if the player
     * choose to create a game, server create a new lobby;<br>
     * if a player decide to join an existing game, server check if there are lobby available,
     * if not ask again the player a joining action;<br>
     * otherwise let the player join an existing lobby.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void askJoiningAction() throws IOException, ClassNotFoundException {
        int joiningActionChosen; // 0 for creating a new match | 1 for joining an existing one if present

        CommunicationMessage messageReceived =  getResponse().get();
        while (messageReceived.getID() != JOINING_ACTION_INFO) {
            send(new CommunicationMessage(JOINING_ACTION_INFO, null));
            messageReceived = getResponse().get();
        }

        joiningActionChosen = (int)messageReceived.getMessage();

        if (joiningActionChosen == 0) {
            send(new CommunicationMessage(CREATE_LOBBY_ACTION_CONFIRMED, null));
            createNewGame();
        } else {
            if(server.getActiveGames().size() <= 0) {
                send(new CommunicationMessage(ERROR, "No lobbies are available."));
                send(new CommunicationMessage(JOINING_ACTION_INFO, null));
                askJoiningAction();
            } else {
                send(new CommunicationMessage(JOIN_LOBBY_ACTION_CONFIRMED, fetchLobbyInfos()));
                joinExistingGame();
            }
        }
    }


    /**
     * create a new lobby asking player the game rules.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void createNewGame() throws IOException, ClassNotFoundException {
        int numberOfPlayer = askGameNumberOfPlayer();
        boolean expertMode = askGameType();
        Lobby newLobby = new Lobby(server, this, numberOfPlayer, expertMode);
        newLobby.registerClientToLobby(this);
        server.addGameLobby(newLobby);
        server.handleLobbyState(newLobby, this);
    }

    /**
     * ask the number of players for the creation of a new game
     * @return number of players chosen
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private int askGameNumberOfPlayer() throws IOException, ClassNotFoundException {
        int size = 0;

        CommunicationMessage messageReceived = getResponse().get();
        while(messageReceived.getID() != NUMBER_OF_PLAYER_INFO) {
            send(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, null));
            messageReceived = getResponse().get();
        }
        size = (int) messageReceived.getMessage();
        send(new CommunicationMessage(NUMBER_OF_PLAYER_CONFIRMED, null));
        return size;
    }

    /**
     * Ask the game type of the current game
     * @return game type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private boolean askGameType() throws IOException, ClassNotFoundException {
        boolean mode = false;

        CommunicationMessage messageReceived = getResponse().get();
        while(messageReceived.getID() != GAME_TYPE_INFO) {
            send(new CommunicationMessage(GAME_TYPE_INFO, null));
            messageReceived = getResponse().get();
        }

        mode = (boolean) messageReceived.getMessage();
        send(new CommunicationMessage(LOBBY_JOINED_CONFIRMED, null));

        return mode;
    }

    /**
     * When a player choose a lobby where to play, if the lobby isn't full, let the player join the lobby.<br>
     * If the lobby is full, notify player and ask another joining action.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void joinExistingGame() throws IOException, ClassNotFoundException {
        CommunicationMessage receivedMessage = getResponse().get();
        while(receivedMessage.getID() != LOBBY_TO_JOIN_INFO) {
            send(new CommunicationMessage(LOBBY_TO_JOIN_INFO, fetchLobbyInfos()));
            receivedMessage = getResponse().get();
        }

        String lobbyChosen = receivedMessage.getMessage().toString();
        Lobby selectedLobby = server.getActiveGames().stream().filter(lobby -> lobby.getLobbyName().equals(lobbyChosen)).toList().get(0);
        if (selectedLobby.isFull()) {
            send(new CommunicationMessage(INFO, "The lobby you selected is already full or got full while you were choosing."));
            send(new CommunicationMessage(JOINING_ACTION_INFO, null));
            askJoiningAction();
        } else {
            send(new CommunicationMessage(LOBBY_JOINED_CONFIRMED, null));
            selectedLobby.registerClientToLobby(this);
            server.handleLobbyState(selectedLobby, this);
        }
    }

    /**
     * Let the lobbies be serializable
     * @return lobby's info to send
     */
    private List<LobbyInfo> fetchLobbyInfos() {
        List<LobbyInfo> lobbyInfosToSend = new ArrayList<>();
        // Setting up the lobbies to a serializable version
        for (Lobby lobby : server.getActiveGames()) {
            lobbyInfosToSend.add(new LobbyInfo(lobby));
        }

        return lobbyInfosToSend;
    }

    /**
     * Given the available decks, let the player choose one of them
     * @param availableDecks list of available decks
     * @return the message with the chosen deck type
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected DeckType askDeckType(List<DeckType> availableDecks) throws IOException, ClassNotFoundException {
        send(new CommunicationMessage(ASK_DECK, availableDecks));

        CommunicationMessage messageReceived = getResponse().get();
        while(messageReceived.getID() != DECK_TYPE_MESSAGE) {
            send(new CommunicationMessage(DECK_TYPE_MESSAGE, availableDecks));
            messageReceived = getResponse().get();
        }

        return (DeckType) messageReceived.getMessage();
    }


    private Optional<CommunicationMessage> getResponse() throws IOException, ClassNotFoundException {
        Optional<CommunicationMessage> message = Optional.empty();
        do {
            synchronized (incomingMessages) {
                message = incomingMessages.size() > 0 ? Optional.of(incomingMessages.removeFirst()) : message;
            }
        } while (message.isEmpty());
        return message;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketClientConnection that = (SocketClientConnection) o;
        return socket.equals(that.socket) && out.equals(that.out) && in.equals(that.in) && server.equals(that.server) && clientName.equals(that.clientName) && connectionStatusHandler.equals(that.connectionStatusHandler) && incomingMessages.equals(that.incomingMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, out, in, server, clientName, connectionStatusHandler, incomingMessages);
    }
}