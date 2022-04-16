package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_controller.CommunicationMessage;

public class MoveMessage {
    private final Player player;
    private final Game game;
    private final CommunicationMessage.MessageType type;
    private final boolean expertMode;

    public MoveMessage(Game game, CommunicationMessage.MessageType type) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.type = type;
        expertMode = false;
    }

    MoveMessage(Game game, CommunicationMessage.MessageType type, boolean expertMode) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.type = type;
        this.expertMode = expertMode;
    }

    public boolean isExpertMode(){ return expertMode; }
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
