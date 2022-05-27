package it.polimi.ingsw.game_controller;

import java.io.Serializable;

/**
 * Class representing the message object exchanged between clients and server.
 *
 * <p>It is characterized by an ID and can contain the object which must be transmitted (e.g. GameBoard).
 */
public class CommunicationMessage implements Serializable{
    private static final long serialVersionUID = 1234567L;

    /**
     * Enumeration representing all the available message types.
     */
    public enum MessageType {
        CONNECTION_CONFIRMED,
        NAME_MESSAGE,
        NAME_CONFIRMED,
        JOINING_ACTION_INFO,
        JOIN_LOBBY_ACTION_CONFIRMED,
        CREATE_LOBBY_ACTION_CONFIRMED,
        NUMBER_OF_PLAYER_INFO,
        NUMBER_OF_PLAYER_CONFIRMED,
        GAME_TYPE_INFO,
        LOBBY_TO_JOIN_INFO,
        LOBBY_JOINED_CONFIRMED,
        NO_LOBBIES_AVAILABLE,
        PLAYER_JOINED_YOUR_LOBBY,
        IS_CHOSING_DECK_TYPE,
        DECK_TYPE_MESSAGE,
        ASK_DECK,
        ASSISTANT_NOT_PLAYABLE,
        GAME_ACTION,
        NOT_YOUR_TURN,
        MOVE_STUDENT_FAILED,
        INVALID_MOTHER_NATURE_STEPS,
        INVALID_CLOUD_CHOSEN,
        NOT_ACTION_PHASE,
        ADVANCED_NOT_PLAYABLE,
        ALREADY_PLAYED_ADVANCED,
        NOT_EXPERT_GAME,
        NOT_ENOUGH_MONEY,
        PLAYER_DISCONNECTED,
        INFO,
        HELP,
        ERROR,
        YOU_LOSE,
        YOU_WIN,
        DRAW,
        VIEW_UPDATE,
        GAME_READY,
        PING,
        PONG,
    }
    private final MessageType ID;
    private final Object message;

    public CommunicationMessage(MessageType id, Object message) {
        this.ID = id;
        this.message = message;
    }

    /**
     * Returns the message ID of the current message.
     *
     * @return the message ID.
     */
    public MessageType getID() {
        return ID;
    }

    /**
     * Returns the message content of the current message.
     *
     * @return the message content.
     */
    public Object getMessage() {
        return message;
    }
}
