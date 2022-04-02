package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

public class DiningHall {
    private final DiningTable[] tables = new DiningTable[5];
    private final List<Teacher> teacherList = new ArrayList<>();

    public DiningHall() {
        tables[ColorCharacter.GREEN.ordinal()] = new DiningTable(ColorCharacter.GREEN);
        tables[ColorCharacter.BLUE.ordinal()] = new DiningTable(ColorCharacter.BLUE);
        tables[ColorCharacter.RED.ordinal()] =  new DiningTable(ColorCharacter.RED);
        tables[ColorCharacter.PINK.ordinal()] = new DiningTable(ColorCharacter.PINK);
        tables[ColorCharacter.YELLOW.ordinal()] = new DiningTable(ColorCharacter.YELLOW);
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
