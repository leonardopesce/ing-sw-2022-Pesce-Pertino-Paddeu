package it.polimi.ingsw.game_model.character.character_utils;

/**
 * An enumeration which represent the type of character card.
 * <dl>
 *     <dt><b>Monk</b> - Cost: <b>1</b></dt>
 *     <dd>
 *         At the beginning of the match, draw 4 students and place them on this card.<br>
 *         <b>EFFECT</b>: Take 1 student from the card and place it on an island of your choice. Then, draw a student from the game bag and place it on this card.
 *     </dd>
 *
 *     <dt><b>Bartender</b> - Cost: <b>2</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: During this turn, you take the control of the other players' professors even if you have the same amount of students in your room as the player currently controlling them.
 *     </dd>
 *
 *     <dt><b>Flagman</b> - Cost: <b>3</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: Chose an island and calculate the majority as if Mother Nature has stopped her movement there.<br>
 *         In this turn Mother Nature will move as usual and on the island she lands, the majority will normally be calculated.
 *     </dd>
 * </dl>
 *
 * @see it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter
 */
public enum AdvancedCharacterType {
    MONK("monk", 1),
    BARTENDER("bartender", 2),
    FLAGMAN("flagman", 3),
    POSTMAN("postman", 1),
    HEALER("healer", 2),
    CENTAURUS("centaurus", 3),
    JESTER("jester", 1),
    BARD("bard", 1),
    KNIGHT("knight", 2),
    PRINCESS("princess", 2),
    LANDLORD("landlord", 3),
    MERCHANT("merchant", 3),
    NULL("null", 0);


    private final String name;
    private final int cardCost;

    AdvancedCharacterType(String name, int cardCost) {
        this.name = name;
        this.cardCost = cardCost;
    }

    public String getCardName() { return this.name; }

    public int getCardCost(){ return cardCost; }
}
