package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final int id;
    private int size;
    private List<Student> students;
    private List<Tower> towers;
    private boolean isBlocked;

    public Island(int id) {
        this.id = id;
        this.size = 1;
        this.isBlocked = false;
        this.students = new ArrayList<>();
        this.towers = new ArrayList<>();

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

    public boolean getIsBlocked() { return isBlocked; }

    public void denyIsland() {
        this.isBlocked = true;
    }

    public void freeIsland() {
        this.isBlocked = false;
    }

    public List<Student> getStudents() {
        return students;
    }
}
