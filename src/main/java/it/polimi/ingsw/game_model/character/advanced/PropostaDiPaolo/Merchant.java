package it.polimi.ingsw.game_model.character.advanced.PropostaDiPaolo;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class Merchant {
    private final Game game;

    public Merchant(Game game) {
        this.game = game;
    }

    public void playEffect(ColorCharacter color) {
        for(Player player : game.getPlayers()) {
            player.getSchool().getDiningHall().getTableOfColor(color).removeStudent(3);
        }
    }
}
