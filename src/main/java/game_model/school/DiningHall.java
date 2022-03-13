package game_model.school;

import game_model.character.basic.Teacher;

import java.util.List;

public class DiningHall {
    private DiningTable[] tables = new DiningTable[5];
    private List<Teacher> teacherList;

    public DiningHall() {
    }

    public DiningTable[] getTables() {
        return tables;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void removeTeacherOfColor(int color){
        for(int i = 0; i < teacherList.size(); i++){
            if(teacherList.get(i).getColor() == color){
                teacherList.remove(i);
                break;
            }
        }
    }
}
