package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_view.board.GameBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdvancedCardInputHandler {
    private final GameViewCLI gameViewCli;
    private final AdvancedCharacterType characterToHandleType;

    public AdvancedCardInputHandler(AdvancedCharacterType characterToHandle, GameViewCLI cli) {
        this.gameViewCli = cli;
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
        int numberOfStudentSelected = 0;
        int currentSelection = 0;
        List<Integer> indexAlreadySelected = new ArrayList<>();

        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        System.out.println("You may exchange up to 2 students between your entrance and your dining room.");

        // Making the player chose the students
        do {
            System.out.println("Choose a student from your entrance. Use a number between (0," + gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size() + "): ");
            //currentSelection = ;
        } while(numberOfStudentSelected < 2 || currentSelection != -1);
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
