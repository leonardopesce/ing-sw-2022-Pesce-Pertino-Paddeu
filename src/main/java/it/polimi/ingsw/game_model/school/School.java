package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;

import java.util.List;

public class School {
    private final DiningHall diningHall = new DiningHall();
    private final Entrance entrance = new Entrance();
    private Integer towersAvailable;

    public School(List<Student> students, int towersAvailable){
        entrance.addAllStudents(students);
        this.towersAvailable = towersAvailable;
    }

    public int getTowersAvailable(){
        return towersAvailable;
    }

    public void setTowersAvailable(int towersAvailable) {
        this.towersAvailable = towersAvailable;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public DiningHall getDiningHall() {
        return diningHall;
    }

    public void addTeacher(Teacher t){
        diningHall.getTeacherList().add(t);
    }
}
