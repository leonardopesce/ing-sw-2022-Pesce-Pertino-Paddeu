package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
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

    /**
     * @param color the color of the table.
     *
     * @see ColorCharacter
     */
    public DiningTable(ColorCharacter color) {
        this.color = color;
    }

    /**
     * Add a student from the entrance to the dining table, only if the table isn't full
     */
    public void addStudent() throws TooManyStudentsException {
        if(numberOfStudents + 1 <= MAX_POSITIONS) {
            numberOfStudents++;
        } else {
            throw new TooManyStudentsException("There are too many students at this table.");
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

    /**
     * Returns the table color.
     * @return the table color.
     */
    public ColorCharacter getColor() {
        return color;
    }

    /**
     * Returns the number of students which are present at the table (range from 0 to 10).
     * @return the number of students which are present at the table.
     */
    public int getNumberOfStudents() {
        return numberOfStudents;
    }
}
