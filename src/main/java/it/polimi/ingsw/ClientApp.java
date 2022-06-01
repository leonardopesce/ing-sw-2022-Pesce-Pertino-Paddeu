package it.polimi.ingsw;

import it.polimi.ingsw.game_view.GameViewCLI;
import it.polimi.ingsw.game_view.GameViewGUI;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Application;

import java.util.Locale;
import java.util.Scanner;

/**
 * The class which makes both the CLI and the GUI runnable.
 *
 * <br>
 * Run the CLI with <pre>{@code java -jar softeng-GC3-ClientApp.jar cli}</pre><br>
 * Run the GUI with <pre>{@code java -jar softeng-GC3-ClientApp.jar gui}</pre>
 *
 */
public class ClientApp{
    private static final boolean gui = true;

    public static void main(String[] args){
        // Checking that the argument is only one and it is either cli or gui,
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
                // The following command is used for resolving a windows 10 resizing issue
                System.setProperty("prism.allowhidpi", "false");
                Application.launch(GameViewGUI.class);
            }
        }
    }
}
