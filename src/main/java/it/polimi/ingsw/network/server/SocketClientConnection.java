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
 * Class to manage the connection between client and server and its status.
 */
public class SocketClientConnection extends Observable<CommunicationMessage> implements ClientConnection, Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;
    private String clientName;
    private final ServerConnectionStatusHandler connectionStatusHandler;
    private final LinkedList<CommunicationMessage> incomingMessages = new LinkedList<>();

    /**
     * @param socket the server socket on which the connection is opened.
     * @param server the server object on which the socket is opened.
     * @throws IOException if an I/O error occurs when the connection is being closed.
     */
    public SocketClientConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.ERROR("Failed to setup input and output socket on SocketClientConnection.", e.getMessage());
        }

        // The actual status of the connection (alive/crashed/disconnected) is actually handled by a special object : ServerConnectionStatusHandler.
        this.connectionStatusHandler = new ServerConnectionStatusHandler();
        this.addObserver(connectionStatusHandler);
        connectionStatusHandler.setConnection(this);
        connectionStatusHandler.start();

        new Thread(() -> {
            try {
                // Waiting for the client to send the nickname chosen by the player.
                askName();
            } catch (Exception e) {
                Logger.ERROR("A problem was found during the player setup. Connection aborted.", e.getMessage());
                close();
            }
        }).start();

    }

    /**
     * Returns whether the connection is active or not.
     * @return true if the current connection with the client is alive, otherwise false.
     */
    public synchronized boolean isActive() {
        return connectionStatusHandler.isConnectionActive();
    }

    /**
     * Writes on the Object output stream the given message.
     * @param message the message which has to be sent to the client.
     * @throws IOException if an I/O error occurs while writing on the socket.
     */
    public synchronized void send(CommunicationMessage message) throws IOException {
        out.reset();
        out.writeObject(message);
        out.flush();
    }

    /**
     * Closes the connection server side.
     *
     * <p>The client gets notified if possible that his connection with the server has been closed.</p>
     */
    @Override
    public synchronized void closeConnection() {
        // Notifying the client if possible (the socket may be already closed).
        try {
            send(new CommunicationMessage(ERROR, "Connection closed"));
        } catch (IOException e) {
            Logger.WARNING("Cannot send a message to " + clientName + " because his socket is already closed maybe due to a disconnection.");
        }

        // Trying to close the socket server side.
        try {
            socket.close();
        } catch (IOException e) {
            Logger.ERROR("Error when closing socket!", e.getMessage());
        }
    }

    /**
     * Closes the connection server side.
     */
    public void close() {
        // If the connection is active:
        if(isActive()) {
            // By killing the status handler (isActive() will then always return false) all the SocketClientConnection functions won't work anymore.
            connectionStatusHandler.kill();

            // Closing effectively the socket.
            closeConnection();
            Logger.INFO("Unregistering " + clientName + "'s connection...");
            server.deregisterConnection(this);
            Logger.INFO(clientName + " has successfully been unregistered.");
        }
    }

    /**
     * Sends the given message to the client asynchronously.
     * @param message the message to be sent.
     */
    @Override
    public synchronized void asyncSend(final CommunicationMessage message) {
        new Thread(() -> {
            try {
                send(message);
            } catch (IOException e) {
                Logger.ERROR("Failed to async send the message..." + clientName + " will now be closed.", e.getMessage());
                close();
            }
        }).start();
    }

    /**
     * Makes the socket client connection start working.
     *
     * <p>While the socket is active, it always passively waits for new messages coming from client.</p>
     */
    @Override
    public void run() {
        try {
            // A notification of the successful connection is sent to the client.
            send(new CommunicationMessage(CONNECTION_CONFIRMED, null));
        } catch (IOException e) {
            Logger.ERROR("Error in enstablishing the connection with the client.", e.getMessage());
        }
        try {
            // While the socket is active it passively waits for new incoming messages.
            while (isActive()) {
                CommunicationMessage message = (CommunicationMessage) in.readObject();
                notify(message);

                // If the incoming message is not a PONG(a pong is processed in a separate object instantly) it is added to a queue.
                if(message.getID() != PONG) {
                    synchronized (incomingMessages) {
                        incomingMessages.add(message);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger.ERROR(clientName + " connection with the remote host has been interrupted.", e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * Waits for the client to send the nickname he has chosen.
     * @throws IOException if an I/O error occurs while writing on the output stream.
     * @throws ClassNotFoundException Thrown when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    private void askName() throws IOException, ClassNotFoundException {
        CommunicationMessage messageReceived = getResponse().get();
        String name = messageReceived.getMessage().toString();

        // If the incoming message is not a name message, or if the nickname has already been taken by another player already connected to the server, or
        // if the nickname is a banned one, than we ask the client to retransmit a new nickname.
        while ((messageReceived.getID() != NAME_MESSAGE) || server.getConnectedPlayersName().contains(name) || Arrays.asList(server.getBannedNicks()).contains(name)) {
            send(new CommunicationMessage(NAME_MESSAGE, null));
            name = getResponse().get().getMessage().toString();
        }

        // If the name is usable, then the client gets notified.
        send(new CommunicationMessage(NAME_CONFIRMED, null));
        clientName = name;
        server.newWaitingConnection(this);

        // Asking the player what to do next: whether creating a new lobby or joining an existing one.
        askJoiningAction();
    }

    /**
     * Ask player if they want to create a game or join an existing one, if the player
     * choose to create a game, server create a new lobby;<br>
     * if a player decide to join an existing game, server check if there are lobbies available,
     * if not ask again the player a joining action;<br>
     * otherwise let the player join an existing lobby.
     * @throws IOException if an I/O error occurs while sending messages to the client.
     * @throws ClassNotFoundException when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    private void askJoiningAction() throws IOException, ClassNotFoundException {
        int joiningActionChosen; // 0 for creating a new match | 1 for joining an existing one if present

        CommunicationMessage messageReceived =  getResponse().get();
        // If the message received is not a joining action, than we ask the client to retransmit the information.
        while (messageReceived.getID() != JOINING_ACTION_INFO) {
            send(new CommunicationMessage(JOINING_ACTION_INFO, null));
            messageReceived = getResponse().get();
        }

        joiningActionChosen = (int)messageReceived.getMessage();

        if (joiningActionChosen == 0) {
            // If the client decides to create a new lobby, he gets notified and the creation process starts.
            send(new CommunicationMessage(CREATE_LOBBY_ACTION_CONFIRMED, null));
            createNewGame();
        } else {
            // Otherwise, we check if there are lobbies available:
            // (1) If some lobby are present, than they'll be sent to the client
            // (2) If there are no lobbies, the client gets asked to retransmit a new joining action.
            if(server.getActiveGames().size() <= 0) {
                send(new CommunicationMessage(NO_LOBBIES_AVAILABLE, "No lobbies are available."));
                send(new CommunicationMessage(JOINING_ACTION_INFO, null));
                askJoiningAction();
            } else {
                send(new CommunicationMessage(JOIN_LOBBY_ACTION_CONFIRMED, fetchLobbyInfos()));
                joinExistingGame();
            }
        }
    }


    /**
     * create a new lobby asking player the game rules and number of players.
     * @throws IOException if an I/O error occurs while creating a new lobby.
     * @throws ClassNotFoundException thrown when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    private void createNewGame() throws IOException, ClassNotFoundException {
        // Waiting passively for the client to send the max size of the lobby.
        int numberOfPlayer = askGameNumberOfPlayer();
        // Waiting passively for the client to send the game type (normal or expert).
        boolean expertMode = askGameType();
        // The new lobby with the fetched information gets created and the owner gets registered to the lobby.
        Lobby newLobby = new Lobby(server, this, numberOfPlayer, expertMode);
        newLobby.registerClientToLobby(this);
        send(new CommunicationMessage(LOBBY_JOINED_CONFIRMED, new LobbyInfo(newLobby)));
        server.addGameLobby(newLobby);
        server.handleLobbyState(newLobby, this);
    }

    /**
     * Wait for the client to send the number of players for the creation of a new game.
     * @return number of players chosen by the client.
     * @throws IOException if an I/O error occurs while fetching the number of players.
     * @throws ClassNotFoundException thrown when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    private int askGameNumberOfPlayer() throws IOException, ClassNotFoundException {
        int size = 0;

        CommunicationMessage messageReceived = getResponse().get();

        // If the incoming message does not contain the correct info, the client is asked to retransmit the information.
        while(messageReceived.getID() != NUMBER_OF_PLAYER_INFO) {
            send(new CommunicationMessage(NUMBER_OF_PLAYER_INFO, null));
            messageReceived = getResponse().get();
        }
        size = (int) messageReceived.getMessage();
        send(new CommunicationMessage(NUMBER_OF_PLAYER_CONFIRMED, null));
        return size;
    }

    /**
     * Waits for the client to send the game type (normal or expert) of the game he is creating.
     * @return true if the game has to be played in expert mode, otherwise false.
     * @throws IOException if an I/O error occurs when fetching the game type.
     */
    private boolean askGameType() throws IOException {
        boolean mode = false;

        CommunicationMessage messageReceived = getResponse().get();

        // If the incoming message is not containing the information about the game type, we ask the client to retransmit it.
        while(messageReceived.getID() != GAME_TYPE_INFO) {
            send(new CommunicationMessage(GAME_TYPE_INFO, null));
            messageReceived = getResponse().get();
        }

        mode = (boolean) messageReceived.getMessage();

        return mode;
    }

    /**
     * When a player choose a lobby where to play, if the lobby isn't full, let the player join the lobby.<br>
     * If the lobby is full, notify player and ask another joining action.
     * @throws IOException if an I/O error occurs when receiving the lobby to join.
     * @throws ClassNotFoundException thrown when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    private void joinExistingGame() throws IOException, ClassNotFoundException {
        CommunicationMessage receivedMessage = getResponse().get();
        // If the message does not contain the identifier of the lobby to join, then the client gets asked to retransmit that information.
        while(receivedMessage.getID() != LOBBY_TO_JOIN_INFO) {
            send(new CommunicationMessage(LOBBY_TO_JOIN_INFO, fetchLobbyInfos()));
            receivedMessage = getResponse().get();
        }

        String lobbyChosen = receivedMessage.getMessage().toString();

        // Go back trick
        if(Arrays.asList(server.getBannedNicks()).contains(lobbyChosen)) {
            send(new CommunicationMessage(INFO, "Back to chosing the joining action."));
            send(new CommunicationMessage(JOINING_ACTION_INFO, null));
            askJoiningAction();
        } else {
            // If the lobby is full, the client gets notified and he will be asked again to choose a joining action.
            Lobby selectedLobby = server.getActiveGames().stream().filter(lobby -> lobby.getLobbyName().equals(lobbyChosen)).toList().get(0);
            if (selectedLobby.isFull()) {
                send(new CommunicationMessage(INFO, "The lobby you selected is already full or got full while you were choosing."));
                send(new CommunicationMessage(JOINING_ACTION_INFO, null));
                askJoiningAction();
            } else {
                // Otherwise, if the lobby is not full, the client gets registered to the selected lobby.
                selectedLobby.registerClientToLobby(this);
                send(new CommunicationMessage(LOBBY_JOINED_CONFIRMED, new LobbyInfo(selectedLobby)));
                server.handleLobbyState(selectedLobby, this);
            }
        }
    }

    /**
     * Let the lobbies be serializable by building an apposite message containing the status of the lobby using only serializable parameters inside.
     * @return lobbies infos to send to the client.
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
     * Given the available decks, let the player choose one of them and waits for the client to choose it passively.
     * @param availableDecks list of available decks.
     * @return the decktype chosen by the client.
     * @throws IOException if an I/O error occurs when receiving the deck type.
     * @throws ClassNotFoundException thrown when an application tries to load in a class through its string name, but no definition for the class with the specified name could be found.
     */
    protected DeckType askDeckType(List<DeckType> availableDecks) throws IOException, ClassNotFoundException {
        send(new CommunicationMessage(ASK_DECK, availableDecks));

        CommunicationMessage messageReceived = getResponse().get();

        // If the incoming message does not contain a deck type, the client is asked to retransmit the message.
        while(messageReceived.getID() != DECK_TYPE_MESSAGE) {
            send(new CommunicationMessage(DECK_TYPE_MESSAGE, availableDecks));
            messageReceived = getResponse().get();
        }

        return (DeckType) messageReceived.getMessage();
    }

    /**
     * Returns the head element of the queue (FIFO logic).
     * @return the head element of the queue (FIFO logic).
     */
    private Optional<CommunicationMessage> getResponse() {
        Optional<CommunicationMessage> message = Optional.empty();

        // While the queue is empty we wait for a message to arrive.
        // If the queue is not empty the first element which was added gets removed from the queue and returned.
        do {
            synchronized (incomingMessages) {
                message = incomingMessages.size() > 0 ? Optional.of(incomingMessages.removeFirst()) : message;
            }
        } while (message.isEmpty());

        return message;
    }

    /**
     * Returns the nickname of the connection.
     * @return the nickname of the connection.
     */
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