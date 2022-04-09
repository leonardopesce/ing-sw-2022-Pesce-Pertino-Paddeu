package it.polimi.ingsw.game_model.character.character_utils;

import java.io.Serializable;

public enum DeckType implements Serializable {

    KING("King Caglioti"), ELDER("Gadioli, il saggio"), PIXIE("Fata Cheru"), SORCERER("Buslacchi, l'Oscuro");

    private static final long serialVersionUID = 1L;
    private final String name;

    DeckType(String name) {this.name=name;}

    public String getName() { return name; }
}
