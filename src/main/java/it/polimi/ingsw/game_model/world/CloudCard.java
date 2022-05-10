package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the cloud card in the game: a cloud card contains students drawn from the bag of students
 * @see it.polimi.ingsw.game_model.character.BagOfStudents
 */
public class CloudCard {
    private final List<Student> studentsOnCloud;

    public CloudCard() {
        this.studentsOnCloud = new ArrayList<>();
    }


    /**
     * Remove students from the cloud card
     * @return
     */
    public List<Student> removeStudentsOnCloud() {
        List<Student> tmp = studentsOnCloud.stream().toList();
        studentsOnCloud.clear();
        return tmp;
    }

    /**
     * Removes students from the cloud
     */
    public void clear() { studentsOnCloud.clear(); }

    /**
     * Given a list of students adds the list on the cloud
     * @param students list of students to add
     */
    public void refill(List<Student> students){
        studentsOnCloud.addAll(students);
    }

    public List<Student> getStudent() {
        return studentsOnCloud;
    }
}
