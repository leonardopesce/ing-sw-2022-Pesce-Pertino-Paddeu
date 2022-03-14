package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.utils.Color;

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
        //@TODO: pick the student randomly
        return new Student(Color.RED);
    }
}
