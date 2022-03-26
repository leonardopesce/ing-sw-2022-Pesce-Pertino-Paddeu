package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

public class CloudCard {
    private final List<Student> studentsOnCloud;

    public CloudCard() {
        this.studentsOnCloud = new ArrayList<>();
    }

    public List<Student> getStudentsOnCloud() {
        return studentsOnCloud;
    }

    public void refill(List<Student> students){
        studentsOnCloud.addAll(students);
    }
}
