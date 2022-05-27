package it.polimi.ingsw;

import it.polimi.ingsw.game_view.GameViewCLI;
import it.polimi.ingsw.game_view.GameViewGUI;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Application;

import java.util.Scanner;

public class ClientApp{
    private static final boolean gui = true;

    public static void main(String[] args){
        /* FOR FUTURE USE (ALREADY TESTED)
        if(args.length != 1 || (!args[0].equals("cli") && !args[0].equals("gui"))) {
            Logger.ERROR("You must pass the correct arguments when launching the client app ('java -jar softeng-GC3-ClientApp.jar cli' or 'java -jar softeng-GC3-ClientApp.jar gui')", "Invalid Args");
            System.exit(-1);
        } else {
            if(args[0].equals("cli")) {
                Logger.INFO("Launching CLI");
                Scanner input = new Scanner(System.in);
                String serverIp;
                int serverPort;
                Logger.INFO("Server ip: ");
                serverIp = input.nextLine();
                Logger.INFO("Server port (default 12347): ");
                serverPort = Integer.parseInt(input.nextLine());
                new GameViewCLI(serverIp, serverPort);
            } else {
                Logger.INFO("Launching GUI");
                System.setProperty("prism.allowhidpi", "false");
                Application.launch(GameViewGUI.class);
            }
        }
        */
        if(gui){
            System.setProperty("prism.allowhidpi", "false");
            Application.launch(GameViewGUI.class);
        }
        else {
            Scanner input = new Scanner(System.in);
            String serverIp;
            int serverPort;
            Logger.INFO("Server ip: ");
            serverIp = input.nextLine();
            Logger.INFO("Server port (default 12347): ");
            serverPort = Integer.parseInt(input.nextLine());
            new GameViewCLI(serverIp, serverPort);
        }
    }
}
