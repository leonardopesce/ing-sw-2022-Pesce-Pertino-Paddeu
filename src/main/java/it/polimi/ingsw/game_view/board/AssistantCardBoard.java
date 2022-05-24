package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.io.Serializable;

/**
 * Manage visualization of assistant cards
 * @see Assistant
 */
public class AssistantCardBoard implements Serializable {
    private final AssistantType type;
    private final int maximumSteps;

    public AssistantCardBoard(Assistant assistant) {
        this.type = assistant.getType();
        this.maximumSteps = assistant.getPossibleSteps();
    }

    public AssistantType getType() {
        return type;
    }

    public int getMaximumSteps() {
        return maximumSteps;
    }
}
