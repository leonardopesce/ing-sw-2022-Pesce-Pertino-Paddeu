package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.custom_exceptions.EmptyDiningTableException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

public class DiningTable {
    private ColorCharacter color;
    private int numberOfStudents = 0;
    private final int MAX_POSITIONS = 10;

    public DiningTable(ColorCharacter color) {
        this.color = color;
    }

    public void addStudent() throws TooManyStudentsException{
        if(numberOfStudents + 1 <= MAX_POSITIONS) {
                numberOfStudents++;
                //@TODO: handle professor logic.


        }
        else throw new TooManyStudentsException("There are no available seats at this table.");
    }

    public void removeStudent() throws EmptyDiningTableException {
        if(numberOfStudents > 0) numberOfStudents--;
        else throw new EmptyDiningTableException("The table is empty. You cannot remove any student.");
    }

    public ColorCharacter getColor() {
        return color;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }
}
