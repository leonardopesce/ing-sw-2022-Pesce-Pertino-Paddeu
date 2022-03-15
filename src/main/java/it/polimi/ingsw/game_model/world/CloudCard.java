package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

public class CloudCard {
    private List<Student> studentsOnCloud;

    public CloudCard() {
        this.studentsOnCloud = new ArrayList<Student>();
    }

    public List<Student> getStudentsOnCloud() {
        return studentsOnCloud;
    }
}
