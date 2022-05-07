package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

/**
 * A class representing the bag of the game.
 * A bag contains:
 * <ul>
 *     <li>unpickedStudents - A list of all the students which are not already been played.</li>
 * </ul>
 */
public class BagOfStudents extends Character{
    private final SecureRandom random = new SecureRandom();
    private final List<Student> unpickedStudents;

    public BagOfStudents(){
        unpickedStudents = new ArrayList<>();
    }

    /**
     * Returns whether the bag is empty or not.
     *
     * @return a boolean value which indicates whether the bag is empty (bag size == 0) or not (bag size > 0).
     */
    public boolean isEmpty(){
        return unpickedStudents.isEmpty();
    }

    /**
     * Adds to the bag 2 students of each color (in total there will be 10 students in the bag after this method's execution).
     */
    public void addStudentsFirstPhase() {
        for(ColorCharacter color : ColorCharacter.values()) {
            for(int i=0; i<2; i++) {
                unpickedStudents.add(new Student(color));
            }
        }

        Collections.shuffle(unpickedStudents);
    }

    /**
     * Adds to the bag the remaining 120 students (24 foreach color).
     */
    public void addStudentsSecondPhase(){
        for(ColorCharacter color : ColorCharacter.values()) {
            for(int i=0; i<24; i++) {
                unpickedStudents.add(new Student(color));
            }
        }

        Collections.shuffle(unpickedStudents);
    }

    /**
     * Draw a random student from the bag.
     *
     * @return a randomly picked student from the bag.
     * @see Student
     */
    public Student drawStudentFromBag() throws BagEmptyException {
        if(!isEmpty()) {
            return this.unpickedStudents.remove(random.nextInt(this.unpickedStudents.size()));
        }
        else throw new BagEmptyException("The bag is empty");
    }

    public List<Student> drawNStudentFromBag(int n) throws BagEmptyException{
        List<Student> temp = new ArrayList<>();
        for(int i = 0; i < n; i++){
            temp.add(drawStudentFromBag());
        }
        return temp;
    }

    public void insertBack(List<Student> students) {
        unpickedStudents.addAll(students);
        Collections.shuffle(unpickedStudents);
    }
}
