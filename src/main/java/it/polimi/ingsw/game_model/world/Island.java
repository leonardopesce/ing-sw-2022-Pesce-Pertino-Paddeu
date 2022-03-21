package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.List;

public class Island {
    private final int id;
    private int size = 1;
    private List<Student> students;
    private List<Tower> towers;

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

    public List<Student> getStudents() {
        return students;
    }
}
