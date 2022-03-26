package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final int id;
    private int size = 1;
    private final List<Student> students = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();
    private int isBlocked = 0;

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

    public boolean isBlocked() { return isBlocked > 0; }

    public void denyIsland() {
        this.isBlocked++;
    }

    public void freeIsland() {
        this.isBlocked--;
    }

    public List<Student> getStudents() {
        return students;
    }
}
