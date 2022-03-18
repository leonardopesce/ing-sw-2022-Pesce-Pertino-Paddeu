package it.polimi.ingsw.game_model.character.character_utils;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;

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
 *
 *     <dt><b>Postman</b> - Cost: <b>1</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: You can move Mother Nature up to 2 additional islands compared to the value of the assistant card you played.
 *     </dd>
 *
 *     <dt><b>Healer</b> - Cost: <b>2</b></dt>
 *     <dd>
 *         At the beginning of the match put the 4 No Entry tiles on this card.<br>
 *         <b>EFFECT</b>: Place a No Entry tile on an island of your choice. The first time Mother Nature ends her movement there put the no entry tile back onto this card without calculating influence of placing any tower.
 *     </dd>
 *
 *     <dt><b>Centaurus</b> - Cost: <b>3</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: When resolving a Conquering on an island, Towers do not count towards influence.
 *     </dd>
 *
 *     <dt><b>Jester</b> - Cost: <b>1</b></dt>
 *     <dd>
 *         At the beginning of the match, draw 6 students and place them on this card.<br>
 *         <b>EFFECT</b>: You may take up to 3 students from this card and replace them with the same number of students from your Entrance.
 *     </dd>
 *
 *     <dt><b>Bard</b> - Cost: <b>1</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: You may exchange up to 2 students between your entrance and your dining room.
 *     </dd>
 *
 *     <dt><b>Knight</b> - Cost: <b>2</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: During the influence calculation this turn, you count as having 2 more influence.
 *     </dd>
 *
 *     <dt><b>Princess</b> - Cost: <b>2</b></dt>
 *     <dd>
 *         At the beginning of the match draw 4 students and place them on this card.
 *         <b>EFFECT</b>: Take 1 student from this card and place it in your dining room. Then, draw a new student from the bag and place it on this card.
 *     </dd>
 *
 *     <dt><b>Landlord</b> - Cost: <b>3</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: Chose a color of student: during the calculation of influence this turn, that color adds no influence.
 *     </dd>
 *
 *     <dt><b>Merchant</b> - Cost: <b>3</b></dt>
 *     <dd>
 *         <b>EFFECT</b>: Choose a type of student: every player (including yourself) must return 3 students of that type from their dining room to the bag. If any player has fewer than 3 students of that type, return as many students as they have.
 *     </dd>
 * </dl>
 *
 * @see AdvancedCharacter
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
