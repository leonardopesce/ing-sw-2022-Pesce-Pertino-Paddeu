package game_model.character;

import game_model.character.basic.Student;

import java.util.List;

public class BagOfStudents{
    private List<Student> unpickedStudents;

    public BagOfStudents(){

    }

    public boolean isEmpty(){
        return unpickedStudents.size() <= 0;
    }

    public void addStudentsSecondPhase(){

    }

    public Student drawStudentFromBag(){
        return new Student(0);
    }
}
