package game_model.school;

import game_model.character.basic.Student;

import java.util.List;

public class Entrance {
    private List<Student> students;
    private final int MAX_NUMBER_OF_STUDENT = 9;

    public Entrance() {
    }

    public List<Student> getStudents() {
        return students;
    }
}
