package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.Healer;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Tower;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class that represents the island piece in the game: islands are the places where mother nature can move and students
 * and towers can be placed, an island can be blocked from an advanced character card
 * If two towers of the same color are placed on islands next to each other, that two islands are equivalent to a single
 * island with incremented size
 */
public class Island {
    private final int id;
    private int size = 1;
    private final List<Student> students = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();
    private IntegerProperty isBlocked = new SimpleIntegerProperty();

    public Island(int id) {
        this.id = id;
    }

    /**
     * Increment size of an island in determined conditions
     */
    public void incrementSize() { this.size++; }

    /**
     * Adds students on an island
     * @param studentToAdd students to add
     */
    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
    }

    /**
     * Useful method to add all students
     * @param students students to add
     */
    public void addAllStudent(List<Student> students) {
        this.students.addAll(students);
    }

    /**
     * Add a list of tower on a island
     * @param towers towers to add
     */
    public void addAllTower(List<Tower> towers){
        this.towers.addAll(towers);
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public boolean isBlocked() { return isBlocked.intValue() > 0; }

    /**
     * Deny player access to place towers or students if the island's blocked
     */
    public void denyIsland() {
        isBlocked.setValue(isBlocked.intValue() + 1);
    }

    /**
     * Free a blocked island
     */
    public void freeIsland() {
        isBlocked.setValue(isBlocked.intValue() - 1);
    }

    public IntegerProperty getIsBlocked(){
        return isBlocked;
    }

    public List<Student> getStudents() {
        return students;
    }
}
