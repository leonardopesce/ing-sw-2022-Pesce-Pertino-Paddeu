package it.polimi.ingsw.game_view;

import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int totalTablesDimension;
        List<Integer> indexSelectedFromEntrance = new ArrayList<>();
        List<Integer> indexSelectedFromDining = new ArrayList<>();
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        // Fetching player current dining tables dimensions
        for(int i=0;i<5;i++) {
            tablesDimensions[i] = gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getTables()[i];
        }
        totalTablesDimension = Arrays.stream(tablesDimensions).sum();

        // Making the player chose the students from his entrance
        if(totalTablesDimension > 0) {
            do {
                System.out.println("Choose a student from your entrance. Use a number between (0," + (gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size() - 1) + "). Insert " + gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size() + " if you want to stop: ");
                currentSelection = gameViewCli.whileInputNotIntegerInRange(0, gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size());
                if (currentSelection == gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size()) {
                    if (numberOfStudentSelectedFromEntrance == 0) {
                        System.out.println("You must select at least 1 student.");
                        currentSelection = -2;
                    }
                } else {
                    if (indexSelectedFromEntrance.contains(currentSelection)) {
                        System.out.println("You have already selected this student.");
                    } else {
                        indexSelectedFromEntrance.add(currentSelection);
                        numberOfStudentSelectedFromEntrance++;
                    }
                }
            } while (numberOfStudentSelectedFromEntrance < totalTablesDimension && numberOfStudentSelectedFromEntrance < 2 && currentSelection != gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size());


            // Making the player chose the students from his dining hall
            do {
                System.out.println("Choose a dining table to take a student from there and place it in your entrance. Please use (0,4) to chose: ");
                currentSelection = gameViewCli.whileInputNotIntegerInRange(0, 4);
                if (tablesDimensions[currentSelection] <= 0) {
                    System.out.println("You cannot move students from that table.");
                } else {
                    numberOfStudentSelectedFromDiningHall++;
                    tablesDimensions[currentSelection]--;
                    indexSelectedFromDining.add(currentSelection);
                }
            } while (numberOfStudentSelectedFromEntrance != numberOfStudentSelectedFromDiningHall);
        } else {
            System.out.println("You cannot select any student since your tables are empty.");
        }

        List<ColorCharacter> tableColors = indexSelectedFromDining.stream().map(enumInt -> ColorCharacter.values()[enumInt]).toList();
        toRet[0] = gameViewCli.getClient().getName();
        toRet[1] = indexSelectedFromEntrance;
        toRet[2] = tableColors;
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
        Integer islandIndex;
        System.out.println("Choose an island on which to calculate the influence on. Please use (0," + (gameViewCli.getBoard().getTerrain().getIslands().size()-1) + ") to select the island: ");

        islandIndex = requestIslandIndexArgs();
        toRet[0] = islandIndex;
        return toRet;
    }

    private Object[] healer() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        Integer islandIndex;
        System.out.println("Choose an island to deny. Please use (0," + (gameViewCli.getBoard().getTerrain().getIslands().size()-1) + ") to select the island: ");

        islandIndex = requestIslandIndexArgs();
        toRet[0] = islandIndex;
        return toRet;
    }

    private Object[] jester() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        int numberOfStudentSelectedFromCard = 0;
        int currentSelection = 0;
        int numberOfStudentSelectedFromEntrance = 0;
        List<Integer> indexSelectedFromCard = new ArrayList<>();
        List<Integer> indexSelectedFromEntrance = new ArrayList<>();

        // Making the player chose up to 3 students from the card
        do {
            System.out.println("Chose a student from the card. Use a number between (0," + (gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize()-1) + "). Insert " + gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize() + " if you want to stop: ");
            currentSelection = gameViewCli.whileInputNotIntegerInRange(0, gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize());
            if(currentSelection == gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize()) {
                if(numberOfStudentSelectedFromCard == 0) {
                    System.out.println("You must select at least 1 student.");
                    currentSelection = -2;
                }
            } else {
                if(indexSelectedFromCard.contains(currentSelection)) {
                    System.out.println("You have already selected this student.");
                } else {
                    indexSelectedFromCard.add(currentSelection);
                    numberOfStudentSelectedFromCard++;
                }
            }
        } while(numberOfStudentSelectedFromCard < 3 && currentSelection != gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize());

        // Making the player chose the students from his Entrance to swap.
        do {
            System.out.println("Choose a student in your entrance. Please use (0," + (gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size()-1) + ") to chose: ");
            currentSelection = gameViewCli.whileInputNotIntegerInRange(0,gameViewCli.getBoard().getSchools().get(gameViewCli.getBoard().getNames().indexOf(gameViewCli.getBoard().getCurrentlyPlaying())).getEntrance().size()-1);
            numberOfStudentSelectedFromEntrance++;
            indexSelectedFromEntrance.add(currentSelection);
        } while (numberOfStudentSelectedFromCard != numberOfStudentSelectedFromEntrance);

        toRet[0] = gameViewCli.getClient().getName();
        toRet[1] = indexSelectedFromCard;
        toRet[2] = indexSelectedFromEntrance;
        return toRet;
    }

    private Object[] knight() {
        return new Object[characterToHandleType.getArgsLength()];
    }

    private Object[] landlord() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        toRet[0] = requestColorCharacterArgs();
        return toRet;
    }

    private Object[] merchant() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        toRet[0] = requestColorCharacterArgs();
        return toRet;
    }

    private Object[] monk() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        Integer selectedStudentFromCard = requestStudentOnCardArgs();
        Integer selectedIsland = requestIslandIndexArgs();

        toRet[0] = selectedIsland;
        toRet[1] = selectedStudentFromCard;
        return toRet;
    }

    private Object[] postman() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];

        toRet[0] = gameViewCli.getClient().getName();
        return toRet;
    }

    private Object[] princess() {
        Object[] toRet = new Object[characterToHandleType.getArgsLength()];
        Integer selectedStudentOnCard = requestStudentOnCardArgs();

        toRet[0] = gameViewCli.getClient().getName();
        toRet[1] = selectedStudentOnCard;
        return toRet;
    }

    private int requestIslandIndexArgs() {
        int currentSelection;

        System.out.println("Chose an island. Use (0," + (gameViewCli.getBoard().getTerrain().getIslands().size()-1) + ") to select:");
        // Asking the player the island index.
        currentSelection = gameViewCli.whileInputNotIntegerInRange(0,gameViewCli.getBoard().getTerrain().getIslands().size()-1);

        return currentSelection;
    }

    private ColorCharacter requestColorCharacterArgs() {
        int currentSelection;

        // Asking the player to pick a color.
        System.out.println("Pick a color from the list below:");
        for(ColorCharacter color : ColorCharacter.values()) {
            System.out.println(color.ordinal() + " - " + color.toString());
        }

        currentSelection = gameViewCli.whileInputNotIntegerInRange(0, ColorCharacter.values().length);
        return ColorCharacter.values()[currentSelection];
    }

    private int requestStudentOnCardArgs() {
        int currentSelection;

        // Asking the player to pick a student from the selected card.
        System.out.println("Chose a student from the card. Please use (0," + (gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize()-1) + ") to select the student (starting counting from left to right and top to bottom).");
        currentSelection = gameViewCli.whileInputNotIntegerInRange(0, gameViewCli.getBoard().getTerrain().getAdvancedCard().stream().filter(acb -> acb.getType().equals(characterToHandleType)).toList().get(0).getStudentsSize());

        return currentSelection;
    }
}
