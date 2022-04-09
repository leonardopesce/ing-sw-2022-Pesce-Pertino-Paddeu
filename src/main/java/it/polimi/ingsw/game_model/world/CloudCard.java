package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

public class CloudCard {
    private final List<Student> studentsOnCloud;

    public CloudCard() {
        this.studentsOnCloud = new ArrayList<>();
    }

    public List<Student> removeStudentsOnCloud() {
        //TODO checl if it empties the island
        List<Student> tmp = studentsOnCloud.stream().toList();
        studentsOnCloud.clear();
        return tmp;
    }

    public void clear() { studentsOnCloud.clear(); }

    public void refill(List<Student> students){
        studentsOnCloud.addAll(students);
    }

    public List<Student> getStudent() {
        return studentsOnCloud;
    }
}
