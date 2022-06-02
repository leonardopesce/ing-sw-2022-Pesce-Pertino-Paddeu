package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.game_view.board.Printable.*;


/**
 * Class representing the game board in a light way to transmit fewer data as possible
 */
public class GameBoard implements Serializable{
    public static final long serialVersionUID = 1L;
    private boolean expertMode = false;
    private final List<SchoolBoard> schools = new ArrayList<>();
    private final TerrainBoard terrain;
    private final List<DeckBoard> decks = new ArrayList<>();
    private final List<Integer> moneys = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private int treasury;
    private final GamePhase phase;
    private final String currentlyPlaying;
    private boolean hasPlayedSpecialCard = false;

    /**
     * Constructor that initializes the game board (school, decks, moneys, players, terrain...)
     * @param game requires a Game as source of representation
     * @see Game
     */
    public GameBoard(Game game) {
        for(Player player: game.getPlayers()){
            schools.add(new SchoolBoard(player.getSchool(), player.getColor(), player.hasPlayedSpecialCard()));
            decks.add(new DeckBoard(player));
            moneys.add(player.getMoney());
            names.add(player.getNickname());
        }

        if(isExpertMode()){
            hasPlayedSpecialCard = game.getCurrentlyPlayingPlayer().hasPlayedSpecialCard();
        }
        terrain = new TerrainBoard(game.getTerrain(), game.getMotherNature().getPosition());
        treasury = 0;
        phase = game.getGamePhase();
        currentlyPlaying = game.getCurrentlyPlayingPlayer().getNickname();

    }

    /**
     * Setter for expert mode game board
     */
    protected void setGameToExpertMode() {
        this.expertMode = true;
    }

    /**
     * Setter for the treasury
     * @param treasury int representing the general treasury
     */
    protected void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    /**
     * Getter for the Decks
     * @return a list of all the player's decks
     * @see DeckBoard
     */
    public List<DeckBoard> getDecks() {
        return decks;
    }

    /**
     * Getter for all the names
     * @return a lists of name of all the player
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Getter for the terrain
     * @return the object representing the terrain
     * @see TerrainBoard
     */
    public TerrainBoard getTerrain() {
        return terrain;
    }

    /**
     * Getter for the currently playing player
     * @return a String containing the name of the currently playing player
     */
    public String getCurrentlyPlaying() {
        return currentlyPlaying;
    }

    /**
     * Getter for the current phase of the game
     * @return the current phase of the game
     */
    public GamePhase getPhase() {
        return phase;
    }

    /**
     * Getter for the current mode of the game
     * @return the current mode of the game (expert or normal)
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Getter for all the Schools in the game
     * @return a list of all the SchoolBoard in game
     * @see SchoolBoard
     */
    public List<SchoolBoard> getSchools() {
        return schools;
    }

    /**
     * Getter for the list of money available for each player
     * @return a list of integer representing the money available to each player
     */
    public List<Integer> getMoneys() {
        return moneys;
    }

    /**
     * A function that pairs a color to the corresponding Printable object
     * @param colorCharacter the input color to be converted
     * @return a string value that represent a color for the CLI
     */
    public static String getColorString(ColorCharacter colorCharacter){
        if(colorCharacter == ColorCharacter.RED){
            return Printable.TEXT_RED;
        }
        else if(colorCharacter == ColorCharacter.GREEN){
            return Printable.TEXT_GREEN;
        }
        else if(colorCharacter == ColorCharacter.YELLOW){
            return Printable.TEXT_YELLOW;
        }
        else if(colorCharacter == ColorCharacter.PINK){
            return Printable.TEXT_PURPLE;
        }
        else{
            return Printable.TEXT_BLUE;
        }
    }

    /**
     * Transform a color of tower to a string
     * @param color of the tower to be converted to String
     * @return a string value that represent a color for the CLI
     */
    public static String getColorTowerString(ColorTower color){
        if(color == ColorTower.WHITE){
            return Printable.TEXT_RESET;
        }
        else if(color == ColorTower.GREY){
            return Printable.TEXT_WHITE;
        }
        else{
            return Printable.TEXT_CYAN;
        }
    }

    /**
     * Convert all the value into a single String ready to be printed inside the CLI
     * @return A String containing all the variable in a graphical way
     */
    @Override
    public String toString(){
        StringBuilder board = new StringBuilder();
        for(int i = 0; i < names.size(); i++){
            String name = names.get(i);
            String spaces = " ".repeat((LENGTH - name.length())  / 2 - 1 - (expertMode ? 1 : 0));
            board.append(NEW_LINE_HIGH).append(V_BAR).append(spaces).append(name).append(spaces)
                    .append((expertMode ? moneys.get(i) + DOLLAR + (moneys.get(i) > 9 ? " " : "  ") : "  "))
                    .append(V_BAR).append("\n");
            board.append(schools.get(i));
        }
        if(expertMode){
            board.append("TREASURY\n")
                .append(TL4_CORNER).append(H4_BAR.repeat(treasury > 9 ? 5 : 4)).append(TR4_CORNER).append("\n")
                .append(V4_BAR).append(" ").append(treasury).append(DOLLAR).append(" ").append(V4_BAR).append("\n")
                .append(BL4_CORNER).append(H4_BAR.repeat(treasury > 9 ? 5 : 4)).append(BR4_CORNER).append("\n");
        }

        board.append(terrain);

        board.append("\n");
        return board.toString();
    }

    /**
     * Getter for knowing if a player as already played a special card in this turn or not
     * @return a boolean false (if the player hasn't played a special card) true (if it did)
     */
    public boolean isHasPlayedSpecialCard() {
        return hasPlayedSpecialCard;
    }
}
