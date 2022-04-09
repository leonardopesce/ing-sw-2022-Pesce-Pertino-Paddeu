package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_view.board.GameBoard;

public class MoveMessage {
    private final Player player;
    private final Game game;
    private Object gameUpdate;

    MoveMessage(Game game, Player player) {
        this.player = player;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }

    public GameBoard getGameBoardUpdate() {
        return new GameBoard(game);
    }
}
