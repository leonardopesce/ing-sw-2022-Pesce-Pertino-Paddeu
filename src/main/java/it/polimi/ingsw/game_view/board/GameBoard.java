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
            String spaces = " ".repeat((Printable.LENGTH - name.length())  / 2 - 1);
            board.append(Printable.NEW_LINE_HIGH).append(Printable.V_BAR).append(spaces).append(name).append(spaces).append("  ").append(Printable.V_BAR).append("\n");
            board.append(schools.get(i));
        }

        board.append(terrain);

        board.append("\n");
        return board.toString();
    }

}
