package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.Character;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;

import java.security.SecureRandom;
import java.util.List;

/**
 * An abstract class to represent all the 12 character cards.
 *
 * @see AdvancedCharacterType
 */
public abstract class AdvancedCharacter extends Character {
    protected final AdvancedCharacterType type;
    protected final Game game;
    protected int cardCost;
    private static final SecureRandom random = new SecureRandom();

    protected AdvancedCharacter(AdvancedCharacterType type, Game game){
        super();
        this.type = type;
        this.game = game;
        this.cardCost = type.getCardCost();
    }

    public String getName(){
        return type.getCardName();
    }

    public AdvancedCharacterType getType() {
        return type;
    }

    public int getCardCost() {
        return cardCost;
    }

    public void incrementCardCost() {
        if(cardCost == getType().getCardCost()) cardCost++;
    }

    public static AdvancedCharacter getRandomCard(Game game, List<AdvancedCharacter> alreadyPresent){
        int value = 6;//random.nextInt(AdvancedCharacterType.values().length - 1);

        while(alreadyPresent.stream().map(character -> character.getType().ordinal()).toList().contains(value)){
            value = random.nextInt(AdvancedCharacterType.values().length - 1);
        }

        return switch (AdvancedCharacterType.values()[value]) {
            case MONK -> new Monk(game);
            case BARTENDER -> new Bartender(game);
            case FLAGMAN -> new Flagman(game);
            case POSTMAN -> new Postman(game);
            case HEALER -> new Healer(game);
            case CENTAURUS -> new Centaurus(game);
            case JESTER -> new Jester(game);
            case KNIGHT -> new Knight(game);
            case MERCHANT -> new Merchant(game);
            case BARD -> new Bard(game);
            case PRINCESS -> new Princess(game);
            case LANDLORD -> new Landlord(game);
            default -> throw new IllegalStateException("Unexpected value for random Expert mode card getter: " + value);
        };
    }

    public abstract boolean playEffect(Object... attributes);
    protected abstract boolean validateArgs(Object... attributes);
}