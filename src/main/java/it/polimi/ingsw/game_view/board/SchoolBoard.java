package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;

import java.util.ArrayList;
import java.util.List;

public class SchoolBoard {
    List<ColorCharacter> entrance = new ArrayList<>();
    List<ColorCharacter> teachers = new ArrayList<>();
    int[] tables = new int[5];
    int towers;

    public SchoolBoard(School school) {
        for(Student student: school.getEntrance().getStudents()){
            entrance.add(student.getColor());
        }
        for(Teacher teacher: school.getDiningHall().getTeacherList()){
            teachers.add(teacher.getColor());
        }
        for(int i = 0; i < 5; i++){
            tables[i] = school.getDiningHall().getTables()[i].getNumberOfStudents();
        }
        towers = school.getTowersAvailable();

    }
}
