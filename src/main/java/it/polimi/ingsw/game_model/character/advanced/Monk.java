package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.network.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class Monk extends AdvancedCharacter{
    private final List<Student> studentsOnCard;

    public Monk(Game game) {
        super(AdvancedCharacterType.MONK, game);
        this.studentsOnCard = new ArrayList<>();

        // Setting up 4 students on the card
        try {
            studentsOnCard.addAll(game.getBag().drawNStudentFromBag(4));
        } catch (BagEmptyException e) {
            // Impossible to reach since the cards are eventually setup at the beginning of the match
            Logger.ERROR("Unable to setup the Monk card.", e.getMessage());
        }
    }

    public List<Student> getStudentsOnCard() {
        return studentsOnCard;
    }

    /**
     * Take 1 student from this card and place it on an island of your choice. Then
     * draw a student from the game bag and place it on this card.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes) {
        if(!validateArgs(attributes)){
            return false;
        }

        Integer islandToPlaceStudentOnIndex = (Integer) attributes[0];
        Island islandToPlaceStudentOn = game.getTerrain().getIslands().get(islandToPlaceStudentOnIndex);
        Integer studentToPick = (Integer) attributes[1];

        // Adding the selected student to the selected island
        islandToPlaceStudentOn.addStudent(studentsOnCard.remove(studentToPick.intValue()));

        // picking up a new student from the bag and putting it on the card
        try {
            studentsOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            Logger.WARNING("Unable to pickup a student from the bag to put it on the MONK card since the bag is empty.");
        }
        return true;
    }

    @Override
    protected boolean validateArgs(Object... args) {
        if(args.length != this.getType().getArgsLength()) {
            return false;
        }
        try {
            // If the 2 args are not 2 integers tha card cannot be played
            Integer islandToPlaceStudentOnIndex = (Integer) args[0];
            Integer studentsToPick = (Integer) args[1];

            // If the args are null or if they are indexes out of bound, the card cannot be played.
            if(islandToPlaceStudentOnIndex == null ||
                    studentsToPick == null ||
                    islandToPlaceStudentOnIndex < 0 ||
                    islandToPlaceStudentOnIndex >= game.getTerrain().getIslands().size() ||
                    studentsToPick < 0 ||
                    studentsToPick >= studentsOnCard.size()
            ) return false;
        } catch(Exception ex) {
            return false;
        }

        return true;
    }
}