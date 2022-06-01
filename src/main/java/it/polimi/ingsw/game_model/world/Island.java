package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Tower;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param id the id of the island (at the beginning of the match id == index in the ring).
     */
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

    /**
     * Returns a list of towers which have been built by the players on the island.
     * @return a list of towers which have been built by the players on the island.
     */
    public List<Tower> getTowers() {
        return towers;
    }

    /**
     * Returns the number of islands connected in this archipelago
     * @return the number of islands connected in this archipelago
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the id of the island (might be different from the index of the island in the ring).
     * @return the id of the island.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns true whether the island has a 'deny' tile on it, false otherwise.
     * @return true whether the island has a 'deny' tile on it, false otherwise.
     *
     * @see it.polimi.ingsw.game_model.character.advanced.Healer
     */
    public boolean isBlocked() { return isBlocked.intValue() > 0; }

    /**
     * Deny player access to build towers on the island if it is blocked by a 'deny' tile.
     */
    public void denyIsland() {
        isBlocked.setValue(isBlocked.intValue() + 1);
    }

    /**
     * Removes a 'deny' tile from the island.
     */
    public void freeIsland() {
        isBlocked.setValue(isBlocked.intValue() - 1);
    }

    /**
     * Returns true whether the island has a 'deny' tile on it, false otherwise.
     * @return true whether the island has a 'deny' tile on it, false otherwise.
     *
     * @see it.polimi.ingsw.game_model.character.advanced.Healer
     */
    public IntegerProperty getIsBlocked(){
        return isBlocked;
    }

    /**
     * Returns a list of students placed on the island.
     * @return a list of students placed on the island.
     */
    public List<Student> getStudents() {
        return students;
    }
}
