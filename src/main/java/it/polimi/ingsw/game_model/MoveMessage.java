package it.polimi.ingsw.game_model;

public class MoveMessage {
    private final Player player;
    private final Game game;
    private final boolean error;
    private final String errorMessage;

    public MoveMessage(Game game, boolean error, String errorMessage) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    MoveMessage(Game game) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        error = false;
        errorMessage = "";
    }

    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }

    public boolean hasError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
