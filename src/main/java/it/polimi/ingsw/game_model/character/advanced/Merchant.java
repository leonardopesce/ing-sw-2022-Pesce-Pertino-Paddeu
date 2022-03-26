package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class Merchant extends AdvancedCharacter{

    public Merchant(Game game) {
        super(AdvancedCharacterType.MERCHANT, game);
    }

    public void playEffect(ColorCharacter color) {
        for(Player player : game.getPlayers()) {
            player.getSchool().getDiningHall().getTableOfColor(color).removeStudent(3);
        }
    }
}
