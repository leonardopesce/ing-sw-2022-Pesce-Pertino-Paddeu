package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.Character;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;

import java.util.Random;

import static it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType.*;

/**
 * An abstract class to represent all the 12 character cards.
 *
 * @see AdvancedCharacterType
 */
public abstract class AdvancedCharacter extends Character {
    private final AdvancedCharacterType type;

    protected AdvancedCharacter(AdvancedCharacterType type){
        super();
        this.type = type;
    }

    public AdvancedCharacterType getAdvanceCharacterType(){
        return type;
    }

    public String getName(){
        return type.getCardName();
    }

    public AdvancedCharacterType getType() {
        return type;
    }

    public static AdvancedCharacter getRandomCard(){
        int value = new Random().nextInt(AdvancedCharacterType.values().length);
        return switch (value) {
            case 0 -> new ColorPickerAdvancedCharacter(LANDLORD);
            case 1 -> new ColorPickerAdvancedCharacter(MERCHANT);
            case 2 -> new StudentStorageAdvancedCharacter(MONK);
            case 3 -> new StudentStorageAdvancedCharacter(JESTER);
            case 4 -> new StudentStorageAdvancedCharacter(PRINCESS);
            case 5 -> new NormalAdvancedCharacter(BARTENDER);
            case 6 -> new NormalAdvancedCharacter(FLAGMAN);
            case 7 -> new NormalAdvancedCharacter(POSTMAN);
            case 8 -> new NormalAdvancedCharacter(HEALER);
            case 9 -> new NormalAdvancedCharacter(CENTAURUS);
            case 10 -> new NormalAdvancedCharacter(BARD);
            case 11 -> new NormalAdvancedCharacter(KNIGHT);
            default -> throw new IllegalStateException("Unexpected value for random Expert mode card getter: " + value);
        };
    }
}