package it.polimi.ingsw.game_model.utils;

/**
 * Enum object to represent game playing phases
 */
public enum GamePhase {
    GAME_PENDING("Gioco in attesa"),
    NEW_ROUND("Nuovo round"),
    PLANNING_PHASE("Fase di pianificazione"),
    ACTION_PHASE_MOVING_STUDENTS("Fase azione : sposta gli studenti"),
    ACTION_PHASE_MOVING_MOTHER_NATURE("Fase azione : muovi madre natura"),
    ACTION_PHASE_CHOOSING_CLOUD("Fase azione : scegli tessera nuvola"),
    GAME_ENDED("Fine partita");

    private final String name;

    GamePhase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
