package it.polimi.ingsw.game_view.board;

/**
 * Contains all the printable symbols and tools used to print the game objects in the CLI.
 *
 * @see <a href="https://www.piliapp.com/symbol/line">Symbols library</a>
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI Colors</a>
 */
public abstract class Printable {
    // ANSI Colors
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";
    public static final String TEXT_ORANGE = "\033[38;2;255;165;0m";
    // Gameboard symbols utils
    public static final int LENGTH = 18*4;
    public static final String TL_CORNER = "┏";
    public static final String TR_CORNER = "┓";
    public static final String BL_CORNER = "┗";
    public static final String BR_CORNER = "┛";
    public static final String ML_CORNER = "┣";
    public static final String MR_CORNER = "┫";
    public static final String T_BAR = "┳";
    public static final String TR_BAR = "┻";
    public static final String V_BAR = "┃";
    public static final String H_BAR = "━";
    public static final String H2_BAR = "—";
    public static final String V2_BAR = "│";
    public static final String TL2_CORNER = "╭";
    public static final String TR2_CORNER = "╮";
    public static final String BL2_CORNER = "╰";
    public static final String BR2_CORNER = "╯";
    public static final String H3_BAR = "☷";
    public static final String V3_BAR = "┋";
    public static final String H4_BAR = "═";
    public static final String V4_BAR = "║";
    public static final String TL4_CORNER = "╔";
    public static final String TR4_CORNER = "╗";
    public static final String BL4_CORNER = "╚";
    public static final String BR4_CORNER = "╝";

    public static final String TEACHER = "◉";
    public static final String STUDENT = "●";
    public static final String TOWER = "⊗";
    public static final String DENY = TEXT_RED + "✖" + TEXT_RESET;
    public static final String MOTHER_NATURE = "✿";
    public static final String DOLLAR = "$";

    public static final String NEW_LINE_HIGH = TL_CORNER + H_BAR.repeat(LENGTH - 1) + TR_CORNER + "\n";
    public static final String NEW_LINE_MID =  ML_CORNER + H_BAR.repeat(LENGTH - 1) + MR_CORNER + "\n";
    public static final String NEW_LINE_LOW = BL_CORNER + H_BAR.repeat(LENGTH - 1) + BR_CORNER + "\n";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
