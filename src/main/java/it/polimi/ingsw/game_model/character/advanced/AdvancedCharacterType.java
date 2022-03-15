package it.polimi.ingsw.game_model.character.advanced;

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
