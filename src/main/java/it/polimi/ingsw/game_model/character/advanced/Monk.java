package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

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
            e.printStackTrace();
        }
    }



    public void playEffect(Island islandToPlaceStudentOn, Integer studentToPick) {
        //game.playerMoveStudentToIsland();
        // Adding the selected student to the selected island
        islandToPlaceStudentOn.addStudent(studentsOnCard.remove(studentToPick.intValue()));

        // picking up a new student from the bag and putting it on the card
        try {
            studentsOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            e.printStackTrace();
        }
    }
}
