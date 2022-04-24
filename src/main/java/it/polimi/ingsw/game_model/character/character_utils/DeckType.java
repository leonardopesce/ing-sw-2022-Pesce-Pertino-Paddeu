package it.polimi.ingsw.game_model.character.character_utils;

import java.io.Serializable;

public enum DeckType implements Serializable {

    KING("King Caglioti", "/img/card_retro/King.jpg"),
    ELDER("Gadioli, il saggio", "/img/card_retro/Elder.jpg"),
    PIXIE("Fata Cheru", "/img/card_retro/Pixie.jpg"),
    SORCERER("Buslacchi, l'Oscuro", "/img/card_retro/Sorcer.jpg");

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String path;

    DeckType(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
}
