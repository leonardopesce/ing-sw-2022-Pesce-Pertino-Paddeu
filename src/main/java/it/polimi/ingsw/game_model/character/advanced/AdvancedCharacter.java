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

    /**
     * @param type the type of the advanced character.
     * @param game the game on which the advanced character is played.
     *
     * @see AdvancedCharacterType
     * @see Game
     */
    protected AdvancedCharacter(AdvancedCharacterType type, Game game){
        super();
        this.type = type;
        this.game = game;
        this.cardCost = type.getCardCost();
    }

    /**
     * Returns the name of the advanced character.
     * @return the name of the advanced character.
     *
     * @see AdvancedCharacterType
     */
    public String getName(){
        return type.getCardName();
    }

    /**
     * Returns the type of the advanced character.
     * @return the type of the advanced character.
     */
    public AdvancedCharacterType getType() {
        return type;
    }

    /**
     * Returns the cost in coin in order to play the advanced character.
     * @return the cost in coin in order to play the advanced character.
     */
    public int getCardCost() {
        return cardCost;
    }

    /**
     * Increment by 1 the cost of the character if it has not been already incremented before, otherwise it remains the same.
     */
    public void incrementCardCost() {
        if(cardCost == getType().getCardCost()) cardCost++;
    }

    /**
     * Extract a random advanced character from all the possibles.
     * @param game the game instance decorated by the advanced character.
     * @param alreadyPresent already picked advanced characters (3 different characters have to be picked at the beginning
     *                       of an expert game).
     * @return the randomly picked advanced character.
     */
    public static AdvancedCharacter getRandomCard(Game game, List<AdvancedCharacter> alreadyPresent){
        int value = 3;//random.nextInt(AdvancedCharacterType.values().length - 1);

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

    /**
     * Plays the effect of the advanced character.
     * @param attributes the arguments requested by the character in order to be successfully played.
     * @return true whether the effect of the card has been played without any error, otherwise false.
     */
    public abstract boolean playEffect(Object... attributes);

    /**
     * Returns true if the arguments are correct in order to play the effect of the advanced character, otherwise false.
     * @param attributes the arguments to check passed to the character in order to play his effect.
     * @return true if the attributes are correct in order to play the effect of the card without errors, otherwise false.
     */
    protected abstract boolean validateArgs(Object... attributes);
}