package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_controller.CommunicationMessage;

/**
 * Class to manage messages between model and controller
 */
public class MoveMessage {
    private final Player player;
    private final Game game;
    private final CommunicationMessage.MessageType type;
    private final boolean expertMode;

    /**
     * @param game the game on which the move has been performed.
     * @param type the type of move done.
     */
    public MoveMessage(Game game, CommunicationMessage.MessageType type) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.type = type;
        expertMode = false;
    }

    /**
     * @param game the game on which the move has been performed.
     * @param type the type of move done.
     * @param expertMode the type of the game.
     */
    MoveMessage(Game game, CommunicationMessage.MessageType type, boolean expertMode) {
        this.player = game.getCurrentlyPlayingPlayer();
        this.game = game;
        this.type = type;
        this.expertMode = expertMode;
    }

    /**
     * Returns whether the game is in expert mode or not.
     * @return true if the game is in expert mode, otherwise false.
     */
    public boolean isExpertMode(){ return expertMode; }

    /**
     * Returns the currently playing player.
     * @return the currently playing player.
     *
     * @see Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the game instance stored in this object.
     * @return the game instance stored in this object.
     *
     * @see Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns the type (/id) of the message.
     * @return the type of the message.
     *
     * @see it.polimi.ingsw.game_controller.CommunicationMessage.MessageType
     */
    public CommunicationMessage.MessageType getType(){
        return type;
    }
}
