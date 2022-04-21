package it.polimi.ingsw;

import it.polimi.ingsw.game_view.GameViewCLI;
import it.polimi.ingsw.game_view.GameViewGUI;
import javafx.application.Application;

public class ClientApp{
    private static final boolean gui = true;
    public static final String IP = "127.0.0.1";
    public static final int port = 12345;
    public static void main(String[] args){
        if(gui){
            Application.launch(GameViewGUI.class);
        }
        else {
            new GameViewCLI();
        }
    }
}
