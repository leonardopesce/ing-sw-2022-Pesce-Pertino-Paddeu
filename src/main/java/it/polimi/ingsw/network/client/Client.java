package it.polimi.ingsw.network.client;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.utils.ClientConnectionStatusHandler;
import it.polimi.ingsw.network.utils.Logger;
import it.polimi.ingsw.observer.Observable;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

/**
 * Represents a Client.
 */
public class Client extends Observable<CommunicationMessage> {
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
    private String name;
    private ClientConnectionStatusHandler connectionStatusHandler;

    /**
     * @param ip the ip of the server on which the client is connected.
     * @param port the port of the server on which the client is connected.
     */
    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * Close the client socket
     */
    public synchronized void close(){
        try{
            // Closing the client means killing the connection status handler and closing the socket client side.
            connectionStatusHandler.kill();
            socketOut.close();
        } catch (IOException e) {
            Logger.ERROR("Error while trying to close the socket.", e.getMessage());
        }
    }


    /**
     * Returns whether the client is active or not.
     * @return true if the client is still connected to the server and the connection is alive, otherwise false.
     *
     * @see ClientConnectionStatusHandler
     */
    public synchronized boolean isActive(){
        return connectionStatusHandler.isConnectionActive();
    }

    /**
     * Given the input stream, create a thread that continuously waits for new messages while the connection is active; if the input is a
     * <code>CommunicationMessage</code>, call the <code>notify</code> method with the message received ({@link ClientMessageObserverHandler} gets notified).
     * If there isn't a server side response, kill the process.
     * @param socketIn the input stream used for reading messages coming from the server.
     * @return the thread used to read data while the connection is active.
     *
     * @see CommunicationMessage
     * @see ClientMessageObserverHandler
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        // A new thread is getting created. This thread will handle the asynchronous read of messages coming from the server.
        Thread t = new Thread(() -> {
            try {
                // This thread works until the client and his connection with the server are active.
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    // The client only reads CommunicationMessages and notifies them to the ClientMessageObserverHandler
                    // which will handle the message and make a proper action in the UI.
                    if(inputObject instanceof CommunicationMessage){
                        notify((CommunicationMessage)inputObject);
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (Exception e){
                Logger.ERROR("Connection interrupted since the socket is now closed server side. Exiting...", e.getMessage());
                close();
                System.exit(1);
            }
        });
        t.start();
        return t;
    }

    /**
     * Create a thread that write the given message on the <code>SocketOutputStream</code>. When the message is written,
     * flush and reset the socket.
     * @param message message to write
     *
     * @see CommunicationMessage
     */
    public synchronized void asyncWriteToSocket(CommunicationMessage message){
        new Thread(() -> {
            try {
                if (isActive()) {
                    socketOut.writeObject(message);
                    socketOut.flush();
                    socketOut.reset();
                }
            }catch(Exception e){
                close();
            }
        }).start();
    }

    /**
     * Effectively connects the client to the server, instantiate the Input and Output streams, creates the client's
     * connection status handler for this client object/connection and then lets a thread wait incoming messages.
     *
     * @throws IOException when I/O errors occur.
     */
    public void run() throws IOException {
        // Loggin into the server
        Socket socket = new Socket(ip, port);
        Logger.INFO("Connection established");
        // Instantiating the streams
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        // Creating the connection status handler and starting it (in order to start receiving pings correctly from server).
        connectionStatusHandler = new ClientConnectionStatusHandler();
        connectionStatusHandler.setClient(this);
        this.addObserver(connectionStatusHandler);
        connectionStatusHandler.start();

        try{
            // Once the client is set up, then a new thread will listen, until the connection is active, to incoming messages.
            Thread t0 = asyncReadFromSocket(socketIn);
            t0.join();
        } catch(InterruptedException | NoSuchElementException e){
            Logger.INFO("Connection closed from the client side");
        } finally {
            close();
            socketIn.close();
            socket.close();
        }
    }

    /**
     * Sets the name of the client, who in this case is a player who joined the server.
     * @param name name of the player.
     * @return player name set.
     */
    public String setName(String name){
        this.name = name;
        return this.name;
    }

    /**
     * Returns the client nickname.
     * @return the client nickname.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the ip of the server on which the client connects.
     * @param ip ip of the server on which the client connects.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    /**
     * Set the port of the server on which the client connects.
     * @param port port of the server on which the client connects
     */
    public void setPort(int port) {
        this.port = port;
    }
}
