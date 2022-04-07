package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.action.GameAction;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class SocketClientConnection extends Observable<Object> implements ClientConnection, Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public synchronized void closeConnection() {
        send("Connection closed!");
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
    public void asyncSend(final Object message){
        new Thread(() -> send(message)).start();
    }

    @Override
    public void run() {
        Scanner in;
        String name;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Welcome!\nWhat is your name?");
            String read = in.nextLine();
            while(server.getConnectedPlayersName().contains(read)){
                send("Sorry the name: \"" + read + " is already chosen. What is your name?");
                read = in.nextLine();
            }
            name = read;
            server.lobby(this, name);
            in.close();
            ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream());
            while(isActive()){
                GameAction action = (GameAction)objectReader.readObject();
                notify(action);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            close();
        }
    }

    public Optional<String[]> askGameType(){
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());

            send("You are the first player! Choose the number of player in the game (2, 3 or 4)");
            String number = in.nextLine();
            while (!number.equals("2") && !number.equals("3") && !number.equals("4")){
                send("You selected something different from 2, 3 or 4!\n Please choose the number of player in the game (2, 3 or 4)");
                number = in.nextLine();
            }

            send("Choose the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)");
            String mode = in.nextLine();
            while (!mode.equals("e") && !mode.equals("n")){
                send("You selected something different from \"e\" or \"n\"!\n Please choose the the game mode! (type \"e\" for expert mode, type \"n\" for normal mode)");
                mode = in.nextLine();
            }
            return Optional.of(new String[]{number, mode});
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return Optional.empty();
    }

    protected DeckType askDeckType(List<DeckType> availableDeck){
        Scanner in;
        Optional<DeckType> type = Optional.empty();
        String deckChooseString = "";

        for(int i = 0; i < availableDeck.size(); i++){
            deckChooseString = deckChooseString.concat(i + " = " + availableDeck.get(i).getName() + " ");
        }

        while(type.isEmpty()){
            try {
                in = new Scanner(socket.getInputStream());

                send("Select your deck, available use number " + deckChooseString);
                int value = Integer.parseInt(in.nextLine());
                while (value <= 0 || value > availableDeck.size()){
                    send("You selected something different from the available choice.\n" +
                            "Select your deck, available use number " + deckChooseString);
                    value = Integer.parseInt(in.nextLine());
                }
                type = Optional.of(availableDeck.get(value));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return type.get();

    }
}
