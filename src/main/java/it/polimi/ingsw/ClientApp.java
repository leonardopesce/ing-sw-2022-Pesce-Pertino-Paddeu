package it.polimi.ingsw;

import it.polimi.ingsw.game_view.GameViewCLI;
import it.polimi.ingsw.game_view.GameViewGUI;
import it.polimi.ingsw.network.utils.Logger;
import javafx.application.Application;

import java.util.Scanner;

public class ClientApp{
    private static final boolean gui = false;

    public static void main(String[] args){
        if(gui){
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
