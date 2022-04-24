package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_view.board.Printable;

public class Logger extends Printable {
    public static void ERROR(String msg, String errorType) {
        String toPrint = "[" + TEXT_RED + "ERROR" + TEXT_RESET + "] " + TEXT_YELLOW + errorType + TEXT_RESET + " : " + msg;
        System.out.println(toPrint);
    }

    public static void INFO(String msg) {
        String toPrint = "[" + TEXT_CYAN + "INFO" + TEXT_RESET + "] " + msg;
        System.out.println(toPrint);
    }

    public static void WARNING(String msg) {
        String toPrint = "[" + TEXT_ORANGE + "WARNING" + TEXT_RESET + "] " + msg;
        System.out.println(toPrint);
    }
}
