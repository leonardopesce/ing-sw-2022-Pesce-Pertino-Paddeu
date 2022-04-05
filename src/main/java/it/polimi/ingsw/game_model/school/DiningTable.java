package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

public class DiningTable {
    private final ColorCharacter color;
    private int numberOfStudents = 0;
    private final int MAX_POSITIONS = 10;

    public DiningTable(ColorCharacter color) {
        this.color = color;
    }

    public void addStudent() {
        //TODO in view block the possibility to add students when table is full
        if(numberOfStudents + 1 <= MAX_POSITIONS) {
            numberOfStudents++;
        }
    }

    public List<Student> removeStudent(int numberToRemove) {
        List<Student> students = new ArrayList<>();
        for(int i=0; i<numberToRemove; i++) {
            if(numberOfStudents > 0){
                numberOfStudents--;
                students.add(new Student(color));
            }
            else break;
        }
        return students;
    }

    public ColorCharacter getColor() {
        return color;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }
}
