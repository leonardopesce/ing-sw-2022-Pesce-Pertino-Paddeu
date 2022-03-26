package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

public class Entrance {
    private final List<Student> students = new ArrayList<>();
    private final int MAX_NUMBER_OF_STUDENT = 9;

    public Entrance() {
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student moveStudent(int student) {
        return students.remove(student);
    }

    public void addAllStudents(List<Student> students) throws TooManyStudentsException {
        if(students.size() + this.students.size() <= MAX_NUMBER_OF_STUDENT){
            this.students.addAll(students);
        }
        else throw new TooManyStudentsException("Trying to add too many students to an entrance");
    }
}
