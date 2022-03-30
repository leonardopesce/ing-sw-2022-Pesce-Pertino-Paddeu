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

public class Island {
    private final int id;
    private int size = 1;
    private final List<Student> students = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();
    private IntegerProperty isBlocked = new SimpleIntegerProperty();

    public Island(int id) {
        this.id = id;
    }

    public void incrementSize() { this.size++; }

    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
    }

    public void addAllStudent(List<Student> students) {
        this.students.addAll(students);
    }

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

    public void denyIsland() {
        isBlocked.setValue(isBlocked.intValue() + 1);
    }

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
