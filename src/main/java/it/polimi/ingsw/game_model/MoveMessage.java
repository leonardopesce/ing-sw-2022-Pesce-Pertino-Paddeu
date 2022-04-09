package it.polimi.ingsw.game_model;

public class MoveMessage {
    private final Player player;
    private final Game game;

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
}
