package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A class to model the entrance of the player's game board: a entrance contains a list of students, that can be
 * received from a cloud card, and can be moved on islands or dining table
 */
public class Entrance {
    private final List<Student> students = new ArrayList<>();

    public Entrance() {
        // No need to build anything.
    }

    /**
     * Returns the list of students which are in the entrance.
     * @return the list of students which are in the entrance.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Returns the student of the given index in the entrance.
     * @param indexOfStudent the index of the student to get from the entrance.
     * @return the student of the given index in the entrance.
     */
    public Student getStudent(int indexOfStudent) {
        return students.get(indexOfStudent);
    }

    /**
     * Move a student from the entrance
     * @param student
     * @return a list of students without the moved ones.
     * @see Student
     * @see DiningTable
     */
    public Student moveStudent(int student) {
        return students.remove(student);
    }

    /**
     * Useful method to add all students of a list on the dining table at once(list of students length must always be
     * less long than the remaining dining table's cells)
     * dining table
     * @param students list of students to add
     */
    public void addAllStudents(List<Student> students){
        if(this.students.size() + students.size() <= 9) this.students.addAll(students);
    }
}
