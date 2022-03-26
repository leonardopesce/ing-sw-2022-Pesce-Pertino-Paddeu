package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.Character;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;

import java.util.Random;

/**
 * An abstract class to represent all the 12 character cards.
 *
 * @see AdvancedCharacterType
 */
public abstract class AdvancedCharacter extends Character {
    private final AdvancedCharacterType type;
    protected final Game game;

    protected AdvancedCharacter(AdvancedCharacterType type, Game game){
        super();
        this.type = type;
        this.game = game;
    }

    public String getName(){
        return type.getCardName();
    }

    public AdvancedCharacterType getType() {
        return type;
    }

    public static AdvancedCharacter getRandomCard(Game game){
        int value = new Random().nextInt(AdvancedCharacterType.values().length);
        return switch (value) {
            case 0 -> new Monk(game);
            case 1 -> new Bartender(game);
            case 2 -> new Flagman(game);
            case 3 -> new Postman(game);
            case 4 -> new Healer(game);
            case 5 -> new Centaurus(game);
            case 6 -> new Jester(game);
            case 7 -> new Knight(game);
            case 8 -> new Merchant(game);
            case 9 -> new Bard(game);
            case 10 -> new Princess(game);
            case 11 -> new Landlord(game);
            default -> throw new IllegalStateException("Unexpected value for random Expert mode card getter: " + value);
        };
    }
}