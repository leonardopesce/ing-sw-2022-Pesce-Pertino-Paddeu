package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.game_controller.CommunicationMessage.MessageType.*;

public class SocketClientConnection extends Observable<CommunicationMessage> implements ClientConnection, Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;

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
        new Thread(this::askName).start();
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(CommunicationMessage message) {
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
        send(new CommunicationMessage(ERROR, "Connection closed"));
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final CommunicationMessage message){
        new Thread(() -> send(message)).start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Sono arrivato " +
                    server.getWaitingConnection().keySet().stream().filter(key -> server.getWaitingConnection().get(key).equals(this)).toList().get(0));
            while(isActive()){
                CommunicationMessage message = (CommunicationMessage)in.readObject();
                //TODO may be a problem in.reset()
                in.reset();
                notify(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public int askGameNumberOfPlayer(){
        int size = 0;
        try {
            send(new CommunicationMessage(ASK_PLAYER_NUMBER, null));
            size = (int)((CommunicationMessage)in.readObject()).getMessage();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return size;
    }

    public boolean askGameType(){
        boolean mode = false;
        try {
            send(new CommunicationMessage(ASK_GAME_TYPE, null));
            mode = (boolean)((CommunicationMessage)in.readObject()).getMessage();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return mode;
    }

    protected DeckType askDeckType(List<DeckType> availableDecks){
        DeckType type = null;
        try {
            send(new CommunicationMessage(ASK_DECK, availableDecks));
            type = (DeckType)getResponse().get().getMessage();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return type;
    }

    private Optional<CommunicationMessage> getResponse(){
        Optional<CommunicationMessage> message = Optional.empty();
        try {
            message = Optional.of((CommunicationMessage)in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void askName(){
        String name;
        try{
            send(new CommunicationMessage(ASK_NAME, null));

            name = ((CommunicationMessage)in.readObject()).getMessage().toString();
            while(server.getConnectedPlayersName().contains(name)){
                send(new CommunicationMessage(REASK_NAME, null));
                name = ((CommunicationMessage)in.readObject()).getMessage().toString();
            }

            server.lobby(this, name);

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketClientConnection that = (SocketClientConnection) o;
        return active == that.active && Objects.equals(socket, that.socket) && Objects.equals(out, that.out) && Objects.equals(in, that.in) && Objects.equals(server, that.server);
    }
}
