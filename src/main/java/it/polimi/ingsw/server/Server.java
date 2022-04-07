package it.polimi.ingsw.server;

import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_view.GameView;
import it.polimi.ingsw.game_view.RemoteGameView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c) {
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        waitingConnection.keySet().removeIf(s -> waitingConnection.get(s) == c);
    }

    //Wait for other players
    public synchronized void lobby(ClientConnection c, String name){
        List<String> keys = new ArrayList<>(waitingConnection.keySet());
        int numberOfPlayer = 2;
        boolean expertMode = false;

        for (String key : keys) {
            ClientConnection connection = waitingConnection.get(key);
            connection.asyncSend("Connected User: " + key);
        }
        waitingConnection.put(name, c);
        if(waitingConnection.size() == 1){
            Optional<String[]> parameter;
            do {
                parameter = ((SocketClientConnection) c).askGameType();
            } while(parameter.isEmpty());
            numberOfPlayer = Integer.parseInt(parameter.get()[0]);
            expertMode = parameter.get()[1].equals("e");
        }

        keys = new ArrayList<>(waitingConnection.keySet());

        if (waitingConnection.size() == numberOfPlayer) {
            Game game = expertMode ? new GameExpertMode(numberOfPlayer) : new Game(numberOfPlayer);
            GameController controller = new GameController(game);
            for(String nameKey: keys){
                ClientConnection connection = waitingConnection.get(nameKey);

                controller.createPlayer(nameKey, ((SocketClientConnection)waitingConnection.get(nameKey)).askDeckType(controller.getAvailableDeckType()));
                GameView view = new RemoteGameView(
                        game.getPlayers().stream().reduce((pl1, pl2) -> pl1.getNickname().equals(nameKey) ? pl1 :pl2).get(),
                        connection
                );
                /*
                 * add game observer
                 *  model.addObserver(view);
                 */
                view.addObserver(controller);
                /*
                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
                waitingConnection.clear();
                */
                connection.asyncSend(game.print());

            }


        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        int connections = 0;
        boolean running = true;
        System.out.println("Server is running");

        while(running){
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                running = false;
                System.out.println("Connection Error!");
            }
        }
    }

    public Set<String> getConnectedPlayersName() {
        return waitingConnection.keySet();
    }
}
