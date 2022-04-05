package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.DeckAssistants;

public abstract class GameView {
    public abstract void showPlayerDeck(Player player, DeckAssistants deck);
    public abstract void showStudentsLeftToMovePlayer(Player player, int toMove);
    public abstract void showNewTeachersOwnership();
    public abstract void showMotherNaturePossibleStep(int possibleSteps);
    public abstract void showNewInfluence();
}
