package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;

public class AdvancedCardInputHandler {
    private final AdvancedCharacterType characterToHandleType;

    public AdvancedCardInputHandler(AdvancedCharacterType characterToHandle) {
        this.characterToHandleType = characterToHandle;
    }

    public Object[] getCardInputs() {
        return switch (characterToHandleType) {
            case BARD -> bard();
            case BARTENDER -> bartender();
            case CENTAURUS -> centaurus();
            case FLAGMAN -> flagman();
            case HEALER -> healer();
            case JESTER -> jester();
            case KNIGHT -> knight();
            case LANDLORD -> landlord();
            case MERCHANT -> merchant();
            case MONK -> monk();
            case POSTMAN -> postman();
            case PRINCESS -> princess();
            case NULL -> new Object[0];
        };
    }

    private Object[] bard() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        System.out.println("You may exchange up to 2 students between your entrance and your dining room.");
        System.out.println();
        return toRet;
    }

    private Object[] bartender() {
        return new Object[characterToHandleType.getArgsLength()];
    }

    private Object[] centaurus() {
        return new Object[characterToHandleType.getArgsLength()];
    }

    private Object[] flagman() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] healer() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] jester() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] knight() {
        return new Object[characterToHandleType.getArgsLength()];
    }

    private Object[] landlord() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] merchant() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] monk() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] postman() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }

    private Object[] princess() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        return toRet;
    }
}
