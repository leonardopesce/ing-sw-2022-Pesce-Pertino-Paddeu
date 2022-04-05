package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

public class Entrance {
    private final List<Student> students = new ArrayList<>();

    public Entrance() {
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student getStudent(int indexOfStudent) {
        return students.get(indexOfStudent);
    }

    public Student moveStudent(int student) {
        return students.remove(student);
    }

    public void addAllStudents(List<Student> students){
        //TODO does this rise a problem in case there are more than 9 students?
        if(this.students.size() + students.size() <= 9) this.students.addAll(students);
    }
}
