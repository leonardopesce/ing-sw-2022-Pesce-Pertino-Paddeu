package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;

import java.io.Serializable;

public class GameBoardAdvanced extends GameBoard implements Serializable {

    public GameBoardAdvanced(Game game) {
        super(game);
        setGameToExpertMode();
        setTreasury(((GameExpertMode)game).getTreasury());
    }
}
