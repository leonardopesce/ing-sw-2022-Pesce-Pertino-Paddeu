package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.List;

public class School {
    private DiningHall diningHall = new DiningHall();
    private List<Assistant> assistants;
    private Entrance entrance = new Entrance();
    private Integer towersAvailable;

    public School() {
    }

    private School(Integer towersAvailable){
        this.towersAvailable = towersAvailable;
    }

    public int getTowersAvailable(){
        return towersAvailable;
    }

    public void setTowersAvailable(int towersAvailable) {
        this.towersAvailable = towersAvailable;
    }

    public List<Assistant> getAssistants() {
        return assistants;
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
