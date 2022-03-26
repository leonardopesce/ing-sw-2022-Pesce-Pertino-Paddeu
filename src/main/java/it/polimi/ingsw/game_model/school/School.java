package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.List;

public class School {
    private DiningHall diningHall = new DiningHall();
    private Entrance entrance = new Entrance();
    private Integer towersAvailable;

    public School(List<Student> students, int towersAvailable) throws TooManyStudentsException{
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
