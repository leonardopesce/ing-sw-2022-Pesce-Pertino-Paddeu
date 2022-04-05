package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class Merchant extends AdvancedCharacter{

    public Merchant(Game game) {
        super(AdvancedCharacterType.MERCHANT, game);
    }

    /**
     * Choose a type of student: every player (including yourself) must return 3 students of that type from their
     * dining room to the bag. If any player has fewer than 3 students of that type, return as many students as they have.
     * @param color color of student chosen
     */
    public void playEffect(ColorCharacter color) {
        for(Player player : game.getPlayers()) {
            game.getBag().insertBack(
                    player.getSchool().getDiningHall().getTableOfColor(color).removeStudent(3)
            );
        }
    }

}
