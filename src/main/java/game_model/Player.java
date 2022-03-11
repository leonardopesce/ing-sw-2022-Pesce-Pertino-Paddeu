package game_model;

import game_model.character.Assistant;
import game_model.school.School;

import java.util.List;

public class Player {
    private final int id;
    private School school = new School();
    private List<Assistant> discardedAssistants;
    private int money = 0;

    public Player(int id) {
        this.id = id;
    }

    public void discardAssistant(){

    }

    public int getId() {
        return id;
    }

    public School getSchool() {
        return school;
    }

    public List<Assistant> getDiscardedAssistants() {
        return discardedAssistants;
    }

    public int getMoney() {
        return money;
    }
}
