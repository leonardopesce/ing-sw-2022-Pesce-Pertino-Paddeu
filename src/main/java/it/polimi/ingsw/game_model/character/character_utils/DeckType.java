package it.polimi.ingsw.game_model.character.character_utils;

import java.io.Serializable;

/**
 * Represents the magicians on the retro of the assistants cards.
 *
 * <p>
 *     There are 4 decks (so 4 magicians) with 10 assistants.
 * </p>
 */
public enum DeckType implements Serializable {

    KING("King Caglioti", "/img/card_retro/King.jpg"),
    ELDER("Gadioli, il saggio", "/img/card_retro/Elder.jpg"),
    PIXIE("Fata Cheru", "/img/card_retro/Pixie.jpg"),
    SORCERER("Buslacchi, l'Oscuro", "/img/card_retro/Sorcer.jpg");

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String path;

    /**
     * @param name the name of the magician card.
     * @param path the path to the image representing the card retro in the resource package.
     */
    DeckType(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     * Returns the magician name.
     * @return the magician name.
     */
    public String getName() { return name; }

    /**
     * Returns the path to the image representing the card retro in the resource package.
     * @return the path to the image representing the card retro in the resource package.
     */
    public String getPath() { return path; }
}
