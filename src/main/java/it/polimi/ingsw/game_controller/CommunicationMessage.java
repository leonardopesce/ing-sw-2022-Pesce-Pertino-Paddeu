package it.polimi.ingsw.game_controller;

import java.io.Serializable;

public class CommunicationMessage implements Serializable{
    private static final long serialVersionUID = 1234567L;
    public enum MessageType {
        ASK_NAME,
        REASK_NAME,
        ASK_DECK,
        ASK_PLAYER_NUMBER,
        ASK_GAME_TYPE,
        GAME_ACTION,
        ERROR
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
