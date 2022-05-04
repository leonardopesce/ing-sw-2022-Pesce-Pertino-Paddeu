package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.character_utils.AssistantType;

import java.io.Serializable;

public class AssistantCardBoard implements Serializable {
    private final AssistantType type;
    private final int maximumSteps;

    public AssistantCardBoard(AssistantType type, int maximumSteps) {
        this.type = type;
        this.maximumSteps = maximumSteps;
    }

    public AssistantType getType() {
        return type;
    }

    public int getMaximumSteps() {
        return maximumSteps;
    }
}
