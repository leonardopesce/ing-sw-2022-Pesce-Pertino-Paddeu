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
        ASSISTANT_NOT_PLAYABLE,
        GAME_ACTION,
        HELP,
        ERROR,
        YOU_LOSE,
        YOU_WIN,
        VIEW_UPDATE,
        GAME_READY
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
