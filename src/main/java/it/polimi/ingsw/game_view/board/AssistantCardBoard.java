package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.io.Serializable;

/**
 * Manage visualization of assistant cards in a light way to transmit fewer data as possible
 * @see Assistant
 */
public class AssistantCardBoard implements Serializable {
    public static final long serialVersionUID = 1L;
    private final AssistantType type;
    private final int maximumSteps;

    /**
     * Constructor class, creates the message representation of the assistant card (co
     * @param assistant requires the assistant card to represent
     */
    public AssistantCardBoard(Assistant assistant) {
        this.type = assistant.getType();
        this.maximumSteps = assistant.getPossibleSteps();
    }

    /**
     * Getter for assistant type
     * @return Assistant type representing the assistant card
     * @see AssistantType
     */
    public AssistantType getType() {
        return type;
    }

    /**
     * Getter for the maximum steps mother nature can take with the selected card
     * @return an int representing the number of steps
     */
    public int getMaximumSteps() {
        return maximumSteps;
    }
}
