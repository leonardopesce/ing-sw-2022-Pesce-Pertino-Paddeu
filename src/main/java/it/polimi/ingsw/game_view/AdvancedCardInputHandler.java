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
        int numberOfStudentSelectedFromEntrance = 0;
        int currentSelection = 0;
        int numberOfStudentSelectedFromDiningHall = 0;
        int[] tablesDimensions = new int[5];
        List<Integer> indexSelectedFromEntrance = new ArrayList<>();
        List<Integer> indexSelectedFromDining = new ArrayList<>();
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        System.out.println("You may exchange up to 2 students between your entrance and your dining room.");

        // Making the player chose the students from his entrance
        do {
            System.out.println("Choose a student from your entrance. Use a number between (0," + gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size() + "). Insert -1 if you want to stop: ");
            currentSelection = gameViewCli.whileInputNotIntegerInRange(-1, gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size());
            if(currentSelection == -1) {
                if(numberOfStudentSelectedFromEntrance == 0) {
                    System.out.println("You must select at least 1 student.");
                    currentSelection = -2;
                }
            } else {
                if(indexSelectedFromEntrance.contains(currentSelection)) {
                    System.out.println("You have already selected this student.");
                } else {
                    indexSelectedFromEntrance.add(currentSelection);
                    numberOfStudentSelectedFromEntrance++;
                }
            }
        } while(numberOfStudentSelectedFromEntrance < 2 || currentSelection != -1);

        // Fetching player current dining tables dimensions
        for(int i=0;i<5;i++) {
            tablesDimensions[i] = gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getTables()[i];
        }

        // Making the player chose the students from his dining hall
        do {
            currentSelection = gameViewCli.whileInputNotIntegerInRange(0,4);
            if(tablesDimensions[currentSelection] <= 0) {
                System.out.println("The table you selected is empty.");
            } else {
                numberOfStudentSelectedFromDiningHall++;
                tablesDimensions[currentSelection]--;
                indexSelectedFromDining.add(currentSelection);
            }
        } while (numberOfStudentSelectedFromEntrance != numberOfStudentSelectedFromDiningHall);

        toRet[0] = gameViewCli.getClient().getName();
        toRet[1] = indexSelectedFromEntrance;
        toRet[2] = indexSelectedFromDining;
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
