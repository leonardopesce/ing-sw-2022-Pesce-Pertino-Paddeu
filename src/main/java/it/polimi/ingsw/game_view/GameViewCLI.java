package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_model.MoveMessage;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.DeckAssistants;

public class GameViewCLI extends GameView{

    protected GameViewCLI(Player player) {
        super(player);
    }

    @Override
    protected void showMessage(Object message) {
        System.out.println(message);
    }

    @Override
    public void update(MoveMessage message) {

    }
}
