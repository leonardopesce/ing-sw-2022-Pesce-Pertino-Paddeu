package it.polimi.ingsw.network.client;

import it.polimi.ingsw.game_controller.CommunicationMessage;
import it.polimi.ingsw.network.utils.ClientConnectionStatusHandler;
import it.polimi.ingsw.network.utils.Logger;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client extends Observable<CommunicationMessage> {
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
    private String name;
    private ClientConnectionStatusHandler connectionStatusHandler;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * Close the client socket
     */
    public synchronized void close(){
        try{
            connectionStatusHandler.kill();
            socketOut.close();
        } catch (IOException e) {
            Logger.ERROR("Error while trying to close the socket.", e.getMessage());
        }
    }


    public synchronized boolean isActive(){
        return connectionStatusHandler.isConnectionActive();
    }

    /**
     * Given the input stream, create a thread that reads it while the connection is active; if the input is a
     * <code>CommunicationMessage</code>, call the <code>notify</code> method with the input stream as input.
     * If there isn't a server side response, kill the process.
     * @param socketIn input from socket
     * @return the thread used to read when it finishes
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    if(inputObject instanceof CommunicationMessage){
                        notify((CommunicationMessage)inputObject);
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (Exception e){
                connectionStatusHandler.kill();
                Logger.ERROR("Connection interrupted since the socket is now closed server side. Exiting...", e.getMessage());
            }
        });
        t.start();
        return t;
    }


    public synchronized void asyncWriteToSocket(CommunicationMessage message){
        new Thread(() -> {
            try {
                if (isActive()) {
                    socketOut.writeObject(message);
                    socketOut.flush();
                    socketOut.reset();
                }
            }catch(Exception e){
                connectionStatusHandler.kill();
            }
        }).start();
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        Logger.INFO("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        connectionStatusHandler = new ClientConnectionStatusHandler();
        connectionStatusHandler.setClient(this);
        this.addObserver(connectionStatusHandler);
        connectionStatusHandler.start();

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            t0.join();
        } catch(InterruptedException | NoSuchElementException e){
            Logger.INFO("Connection closed from the client side");
        } finally {
            connectionStatusHandler.kill();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

    public String setName(String name){
        this.name = name;
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
