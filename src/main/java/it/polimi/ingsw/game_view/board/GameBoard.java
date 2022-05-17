package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.utils.GamePhase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.game_view.board.Printable.*;

public class GameBoard implements Serializable{
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

    public List<Integer> getMoneys() {
        return moneys;
    }

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

    public boolean isHasPlayedSpecialCard() {
        return hasPlayedSpecialCard;
    }
}
