package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolBoard implements Serializable {
    List<ColorCharacter> entrance = new ArrayList<>();
    List<ColorCharacter> teachers = new ArrayList<>();
    int[] tables = new int[5];
    int towers;
    ColorTower towerColor;

    public SchoolBoard(School school, ColorTower colorTower) {
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
        towerColor = colorTower;
    }

    public List<ColorCharacter> getEntrance() {
        return entrance;
    }

    public String print() {
        StringBuilder school = new StringBuilder();
        int studentIndex = 0, towerIndex = 0;
        school.append(GameBoard.ML_CORNER).append(GameBoard.H_BAR.repeat(3*4 - 1))
                .append(GameBoard.T_BAR).append(GameBoard.H_BAR.repeat(GameBoard.LENGTH - 24))
                .append(GameBoard.T_BAR).append(GameBoard.H_BAR.repeat(3*4 - 2))
                .append(GameBoard.MR_CORNER).append("\n");
        for(int i = 0; i < tables.length; i++){
            school.append(GameBoard.V_BAR);
            school.append(addStudentEntrance(studentIndex++));
            school.append(addStudentEntrance(studentIndex++));
            school.append("\t").append(GameBoard.V_BAR);

            school.append(addTable(i));

            school.append(addTower(towerIndex++));
            school.append(addTower(towerIndex++));
            school.append("\t").append(GameBoard.V_BAR);

            school.append("\n");
        }
        school.append(GameBoard.BL_CORNER).append(GameBoard.H_BAR.repeat(3*4 - 1))
                .append(GameBoard.TR_BAR).append(GameBoard.H_BAR.repeat(GameBoard.LENGTH - 24))
                .append(GameBoard.TR_BAR).append(GameBoard.H_BAR.repeat(3*4 - 2))
                .append(GameBoard.BR_CORNER).append("\n");

        return school.toString();
    }

    private String addStudentEntrance(int index){
        return "\t" + (entrance.size() > index ? GameBoard.getColorString(entrance.get(index)) + GameBoard.STUDENT : " ") +
                GameBoard.TEXT_RESET;
    }

    private String addTower(int index){
        return "\t" + (towers > index ? GameBoard.getColorTowerString(towerColor) + GameBoard.TOWER : " ") + GameBoard.TEXT_RESET;
    }

    private String addTable(int index){
        StringBuilder temp = new StringBuilder();
        temp.append("\t");
        for(int i = 0; i < 10; i++){
            temp.append(tables[index] > i ? GameBoard.getColorString(ColorCharacter.values()[index]) + GameBoard.STUDENT : " ").append(GameBoard.TEXT_RESET);
            temp.append("\t");
        }
        temp.append(teachers.contains(ColorCharacter.values()[index]) ? GameBoard.getColorString(ColorCharacter.values()[index]) + GameBoard.TEACHER : " ").append(GameBoard.TEXT_RESET);
        temp.append("\t").append(" ").append(GameBoard.V_BAR);
        return temp.toString();
    }
}
