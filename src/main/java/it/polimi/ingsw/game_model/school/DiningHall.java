package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * class to manage the group of dining tables the player has: each player has 5 tables and a list of teachers
 */

public class DiningHall {
    private final DiningTable[] tables = new DiningTable[5];
    private final List<Teacher> teacherList = new ArrayList<>();

    public DiningHall() {
        tables[ColorCharacter.GREEN.getTableOrder()] = new DiningTable(ColorCharacter.GREEN);
        tables[ColorCharacter.BLUE.getTableOrder()] = new DiningTable(ColorCharacter.BLUE);
        tables[ColorCharacter.RED.getTableOrder()] =  new DiningTable(ColorCharacter.RED);
        tables[ColorCharacter.PINK.getTableOrder()] = new DiningTable(ColorCharacter.PINK);
        tables[ColorCharacter.YELLOW.getTableOrder()] = new DiningTable(ColorCharacter.YELLOW);
    }

    public DiningTable getTableOfColor(ColorCharacter color){
        return tables[color.ordinal()];
    }
    public DiningTable[] getTables() {
        return tables;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }
}
