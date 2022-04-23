package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class SocketClientConnection extends Observable<CommunicationMessage> implements ClientConnection, Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;
    private String clientName;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try{
                askName();
            }
            catch (Exception e){
                System.out.println("An error occurred while you were inserting your name.");
                e.printStackTrace();
                close();
            }
        }).start();
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(CommunicationMessage message) throws SocketException{
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void closeConnection() {
        try {
            send(new CommunicationMessage(ERROR, "Connection closed"));
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("Unregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final CommunicationMessage message){
        new Thread(() -> {
            try {
                send(message);
            } catch (SocketException e) {
                server.deregisterConnection(this);
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            while(isActive()){
                CommunicationMessage message = (CommunicationMessage)in.readObject();
                notify(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection interrupted.");
            server.deregisterConnection(this);
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public void askJoiningAction() throws IOException, ClassNotFoundException{
        int joiningActionChosen; // 0 for creating a new match | 1 for joining an existing one if present

        send(new CommunicationMessage(ASK_JOINING_ACTION, null));
        joiningActionChosen = (int)((CommunicationMessage)in.readObject()).getMessage();

        if (joiningActionChosen == 0) {
            createNewGame();
        } else {
            joinExistingGame();
        }
    }

    public int askGameNumberOfPlayer() throws IOException, ClassNotFoundException{
        int size = 0;
        send(new CommunicationMessage(ASK_PLAYER_NUMBER, null));
        size = (int)((CommunicationMessage)in.readObject()).getMessage();

        return size;
    }

    public boolean askGameType() throws IOException, ClassNotFoundException{
        boolean mode = false;
        send(new CommunicationMessage(ASK_GAME_TYPE, null));
        mode = (boolean)((CommunicationMessage)in.readObject()).getMessage();

        return mode;
    }

    protected DeckType askDeckType(List<DeckType> availableDecks) throws SocketException{
        DeckType type = null;
        send(new CommunicationMessage(ASK_DECK, availableDecks));
        type = (DeckType)getResponse().get().getMessage();

        return type;
    }

    private Optional<CommunicationMessage> getResponse(){
        Optional<CommunicationMessage> message = Optional.empty();
        try {
            message = Optional.of((CommunicationMessage)in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            close();
        }
        return message;
    }

    private void askName() throws IOException, ClassNotFoundException{
        String name;
        send(new CommunicationMessage(ASK_NAME, null));

        name = ((CommunicationMessage)in.readObject()).getMessage().toString();
        while(server.getConnectedPlayersName().contains(name)){
            send(new CommunicationMessage(REASK_NAME, null));
            name = ((CommunicationMessage)in.readObject()).getMessage().toString();
        }

        clientName = name;
        server.newWaitingConnection(this);
    }

    private String askChoseLobby() throws SocketException{
        String chosenLobby;

        List<LobbyInfo> lobbyInfosToSend = new ArrayList<>();
        // Setting up the lobbies to a serializable version
        for(Lobby lobby : server.getActiveGames()) {
            lobbyInfosToSend.add(new LobbyInfo(lobby));
        }
        send(new CommunicationMessage(ASK_LOBBY_TO_JOIN, lobbyInfosToSend));
        chosenLobby = (String)getResponse().get().getMessage();

        return chosenLobby;
    }

    private void createNewGame() throws IOException, ClassNotFoundException{
        int numberOfPlayer = askGameNumberOfPlayer();
        boolean expertMode = askGameType();
        Lobby newLobby = new Lobby(server,this, numberOfPlayer, expertMode);
        newLobby.registerClientToLobby(this);
        server.addGameLobby(newLobby);
        server.handleLobbyState(newLobby);
    }

    private void joinExistingGame() throws IOException, ClassNotFoundException{
        if(server.getActiveGames().size() <= 0) {
            send(new CommunicationMessage(ERROR, "No lobbies are available."));
            askJoiningAction();
        } else {
            String lobbyChosen = askChoseLobby();
            Lobby selectedLobby = server.getActiveGames().stream().filter(lobby -> lobby.getLobbyName().equals(lobbyChosen)).toList().get(0);
            if(selectedLobby.isFull()) {
                send(new CommunicationMessage(ERROR, "The lobby you selected is already full or got full while you were choosing."));
                askJoiningAction();
            } else {
                selectedLobby.registerClientToLobby(this);
                server.handleLobbyState(selectedLobby);
            }
        }
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketClientConnection that = (SocketClientConnection) o;
        return active == that.active && Objects.equals(socket, that.socket) && Objects.equals(out, that.out) && Objects.equals(in, that.in) && Objects.equals(server, that.server);
    }
}
