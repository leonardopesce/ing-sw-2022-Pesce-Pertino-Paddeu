package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private boolean gameType = false;
    private final List<SchoolBoard> schools = new ArrayList<>();
    private final TerrainBoard terrain;
    private final List<DeckBoard> decks = new ArrayList<>();
    private final List<Integer> moneys = new ArrayList<>();
    private int treasury;

    public GameBoard(Game game) {
        for(Player player: game.getPlayers()){
            schools.add(new SchoolBoard(player.getSchool()));
            decks.add(new DeckBoard(player));
            moneys.add(player.getMoney());
        }
        terrain = new TerrainBoard(game.getTerrain());
        treasury = 0;
    }

    protected void setGameToExpertMode() {
        this.gameType = true;
    }

    protected void setTreasury(int treasury) {
        this.treasury = treasury;
    }

    public void print(){
        
    }
}
