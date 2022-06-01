package it.polimi.ingsw.network.utils;

import it.polimi.ingsw.game_view.board.Printable;

/**
 * Used for fancy logging in the console with colors :D.
 */
public class Logger extends Printable {
    /**
     * Prints an error message on the console with the <code>[ERROR]</code> tag.
     * @param msg the error message to print.
     * @param errorType the error type of the message.
     */
    public static void ERROR(String msg, String errorType) {
        String toPrint = "[" + TEXT_RED + "ERROR" + TEXT_RESET + "] " + TEXT_YELLOW + errorType + TEXT_RESET + " : " + msg;
        System.out.println(toPrint);
    }

    /**
     * Prints an informative message on the console with the <code>[INFO]</code> tag.
     * @param msg the informative message to print.
     */
    public static void INFO(String msg) {
        String toPrint = "[" + TEXT_CYAN + "INFO" + TEXT_RESET + "] " + msg;
        System.out.println(toPrint);
    }

    /**
     * Prints a warning message on the console with the <code>[WARNING]</code> tag.
     * @param msg the warning message to print.
     */
    public static void WARNING(String msg) {
        String toPrint = "[" + TEXT_ORANGE + "WARNING" + TEXT_RESET + "] " + msg;
        System.out.println(toPrint);
    }

    /**
     * Prints a game action message on the console with the <code>[GAME LOG]</code> tag and specifying which player
     * made that action.
     * @param msg the message containing the game action to print.
     * @param connectionName the nickname of the player who made the action.
     */
    public static void GAME_LOG(String msg, String connectionName) {
        String toPrint = "[" + TEXT_PURPLE + "GAME LOG" + TEXT_RESET + "] " + TEXT_YELLOW + connectionName + TEXT_RESET + " : " + msg;
        System.out.println(toPrint);
    }
}
