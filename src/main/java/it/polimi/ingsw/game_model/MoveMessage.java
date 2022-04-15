package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_controller.CommunicationMessage;

public class MoveMessage {
    private final Player player;
    private final Game game;
    private final CommunicationMessage.MessageType type;

    public MoveMessage(Game game, CommunicationMessage.MessageType type) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }
    public Game getGame() {
        return game;
    }
    public CommunicationMessage.MessageType getType(){
        return type;
    }
}
