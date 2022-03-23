package it.polimi.ingsw.game_model.game_type;

public class AdvancedGame extends Game{
    public static final int NUMBER_OF_ADVANCED_CARD = 3;

    public AdvancedGame(int playerNums) {
        super(playerNums);
    }

    @Override
    protected void setupBoard() {
        super.setupBoard();

        // pick 3 advanced Character if it's a Expert mode game
        pickAdvancedCards();
    }
}
