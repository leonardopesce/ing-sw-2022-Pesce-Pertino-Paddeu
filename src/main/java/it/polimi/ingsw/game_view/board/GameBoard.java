package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Serializable{
    // Define color constants
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";
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

    public static final String NEW_LINE_HIGH = TL_CORNER + H_BAR.repeat(LENGTH - 1) + TR_CORNER + "\n";
    public static final String NEW_LINE_MID =  ML_CORNER + H_BAR.repeat(LENGTH - 1) + MR_CORNER + "\n";
    public static final String NEW_LINE_LOW = BL_CORNER + H_BAR.repeat(LENGTH - 1) + BR_CORNER + "\n";

    public static final String TEACHER = "◉";
    public static final String STUDENT = "●";
    public static final String TOWER = "⊗";
    public static final String DENY = TEXT_RED + "✖" + TEXT_RESET;

    private boolean expertMode = false;
    private final List<SchoolBoard> schools = new ArrayList<>();
    private final TerrainBoard terrain;
    private final List<DeckBoard> decks = new ArrayList<>();
    private final List<Integer> moneys = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private int treasury;
    private final GamePhase phase;
    private final String currentlyPlaying;

    public GameBoard(Game game) {
        for(Player player: game.getPlayers()){
            schools.add(new SchoolBoard(player.getSchool(), player.getColor()));
            decks.add(new DeckBoard(player));
            moneys.add(player.getMoney());
            names.add(player.getNickname());
        }
        terrain = new TerrainBoard(game.getTerrain());
        treasury = 0;
        phase = game.getGamePhase();
        currentlyPlaying = game.getCurrentlyPlayingPlayer().getNickname();
    }

    protected void setGameToExpertMode() {
        this.expertMode = true;
    }

    protected void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    public List<DeckBoard> getDecks() {
        return decks;
    }

    public List<String> getNames() {
        return names;
    }

    public TerrainBoard getTerrain() {
        return terrain;
    }

    public String getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public List<SchoolBoard> getSchools() {
        return schools;
    }

    public String print(){
        StringBuilder board = new StringBuilder();
        for(int i = 0; i < names.size(); i++){
            String name = names.get(i);
            String spaces = " ".repeat((LENGTH - name.length())  / 2 - 1);
            board.append(NEW_LINE_HIGH + V_BAR + spaces + name + spaces + "  " + V_BAR + "\n");
            board.append(schools.get(i).print());
        }

        board.append(terrain.print());

        board.append("\n");
        return board.toString();
    }


    public static String getColorString(ColorCharacter colorCharacter){
        if(colorCharacter == ColorCharacter.RED){
            return TEXT_RED;
        }
        else if(colorCharacter == ColorCharacter.GREEN){
            return TEXT_GREEN;
        }
        else if(colorCharacter == ColorCharacter.YELLOW){
            return TEXT_YELLOW;
        }
        else if(colorCharacter == ColorCharacter.PINK){
            return TEXT_PURPLE;
        }
        else{
            return TEXT_BLUE;
        }
    }

    public static String getColorTowerString(ColorTower color){
        if(color == ColorTower.WHITE){
            return TEXT_RESET;
        }
        else if(color == ColorTower.GREY){
            return TEXT_WHITE;
        }
        else{
            return TEXT_CYAN;
        }
    }

}
