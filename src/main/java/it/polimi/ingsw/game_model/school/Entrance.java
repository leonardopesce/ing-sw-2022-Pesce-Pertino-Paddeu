package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.List;

public class Entrance {
    private List<Student> students;
    private final int MAX_NUMBER_OF_STUDENT = 9;

    public Entrance() {
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student moveStudent(int student) {
        Student tmpStudent = students.get(student);
        students.remove(student);
        return tmpStudent;
    }
}
