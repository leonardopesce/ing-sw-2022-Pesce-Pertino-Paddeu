package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.Player;

import java.io.Serializable;

/**
 * Class representing the game board in advanced mode in a light way to transmit fewer data as possible
 */
public class GameBoardAdvanced extends GameBoard implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * Constructor that initializes the game board (school, decks, moneys, players, terrain...) and sets the game in
     * expert mode, consequentially setting money to all players.
     * @param game requires a Game as source of representation
     * @see Game
     */
    public GameBoardAdvanced(Game game) {
        super(game);
        setGameToExpertMode();
        setTreasury(((GameExpertMode)game).getTreasury());
    }
}
