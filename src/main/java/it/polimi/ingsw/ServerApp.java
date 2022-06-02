package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.utils.Logger;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class ServerApp
{
    public static void main( String[] args )
    {
        Server server;
        try {
            if(args.length != 1 || (Integer.parseInt(args[0]) < 1024 || Integer.parseInt(args[0]) > 65535)) {
                throw new NumberFormatException();
            } else {
                try {
                    server = new Server(Integer.parseInt(args[0]));
                    server.run();
                } catch (IOException e) {
                    System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
                    System.exit(-1);
                }
            }
        } catch (NumberFormatException invalidArg) {
            Logger.ERROR("In order to run the server you have to specify its port. Try to launch it with 'java -jar softeng-GC3-ServerApp.jar 12347'.", "Invalid args");
            System.exit(-1);
        }
    }
}
