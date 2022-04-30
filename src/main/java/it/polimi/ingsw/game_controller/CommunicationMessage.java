package it.polimi.ingsw.game_controller;

import java.io.Serializable;

public class CommunicationMessage implements Serializable{
    private static final long serialVersionUID = 1234567L;
    public enum MessageType {
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
        DECK_TYPE_MESSAGE,
        ASK_DECK,
        ASSISTANT_NOT_PLAYABLE,
        GAME_ACTION,
        INFO,
        HELP,
        ERROR,
        YOU_LOSE,
        YOU_WIN,
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

    public MessageType getID() {
        return ID;
    }

    public Object getMessage() {
        return message;
    }
}
