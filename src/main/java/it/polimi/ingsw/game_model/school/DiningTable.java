package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * DiningTable class manages the dining table on the game board: a table contains at max 10 students each having the same
 * color
 */
public class DiningTable {
    private final ColorCharacter color;
    private int numberOfStudents = 0;
    public static final int MAX_POSITIONS = 10;

    public DiningTable(ColorCharacter color) {
        this.color = color;
    }

    /**
     * Add a student from the entrance to the dining table, only if the table isn't full
     */
    public void addStudent(){
        //TODO in view block the possibility to add students when table is full
        if(numberOfStudents + 1 <= MAX_POSITIONS) {
            numberOfStudents++;
        }
    }

    /**
     * <p>
     * Remove a specified number of students from the dining table
     * @param numberToRemove number of students to remove
     * @return new list of students without the removed ones
     */
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
