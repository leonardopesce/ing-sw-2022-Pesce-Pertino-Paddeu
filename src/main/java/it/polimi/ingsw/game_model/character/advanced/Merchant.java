package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.Island;

import java.awt.*;

public class Merchant extends AdvancedCharacter{

    public Merchant(Game game) {
        super(AdvancedCharacterType.MERCHANT, game);
    }

    /**
     * Choose a type of student: every player (including yourself) must return 3 students of that type from their
     * dining room to the bag. If any player has fewer than 3 students of that type, return as many students as they have.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes) {
        if (!validateArgs(attributes)) {
            return false;
        }

        ColorCharacter color = (ColorCharacter) attributes[0];
        for (Player player : game.getPlayers()) {
            game.getBag().insertBack(
                    player.getSchool().getDiningHall().getTableOfColor(color).removeStudent(3)
            );
        }

        return true;

    }

    @Override
    protected boolean validateArgs(Object... args) {
        if(args.length != 1){
            return false;
        }
        try {
            ColorCharacter color = (ColorCharacter) args[0];
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
