package it.polimi.ingsw.game_model.character.character_utils;

public enum DeckType {
    KING("King Caglioti"), ELDER("Gadioli, il saggio"), PIXIE("Fata Cheru"), SORCERER("Buslacchi, l'Oscuro");

    private final String name;

    DeckType(String name) {this.name=name;}


    public String getName() { return name; }
}
