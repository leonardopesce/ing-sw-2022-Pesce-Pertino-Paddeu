package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.utils.Color;

public class DiningTable {
    private Color color;
    private int numberOfStudents = 0;
    private final int MAX_POSITIONS = 10;

    public DiningTable(Color color) {
        this.color = color;
    }

    public void addStudent(){
        numberOfStudents++;
    }

    public Color getColor() {
        return color;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }


}
