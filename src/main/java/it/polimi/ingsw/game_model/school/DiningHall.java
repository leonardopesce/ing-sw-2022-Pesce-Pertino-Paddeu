package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.List;

public class DiningHall {
    private DiningTable[] tables = new DiningTable[5];
    private List<Teacher> teacherList;

    public DiningHall() {
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

    public void removeTeacherOfColor(ColorCharacter color){
        for(int i = 0; i < teacherList.size(); i++){
            if(teacherList.get(i).getColor() == color){
                teacherList.remove(i);
                break;
            }
        }
    }
}
