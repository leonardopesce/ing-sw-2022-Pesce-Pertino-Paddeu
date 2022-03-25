package it.polimi.ingsw.game_model.character.advanced.PropostaDiPaolo;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

import java.util.ArrayList;
import java.util.List;

public class Bard {
    private final Game game;
    private final List<Student> studentsOnCard;

    public Bard(Game game) {
        this.game = game;
        this.studentsOnCard = new ArrayList<>();

        // Setting up 4 students on the card
        studentsOnCard.addAll(game.getBag().drawNStudentFromBag(4));
    }

    public void playEffect(Island islandToPlaceStudentOn, int studentToPick) {
        // Adding the selected student to the selected island
        islandToPlaceStudentOn.addStudent(studentsOnCard.remove(studentToPick));

        // picking up a new student from the bag and putting it on the card
        try {
            studentsOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            e.printStackTrace();
        }
    }
}
