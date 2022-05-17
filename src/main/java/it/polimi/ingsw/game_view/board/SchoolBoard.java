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
    boolean advancedAlreadyPlayedThisTurn = false;

    public SchoolBoard(School school, ColorTower colorTower, boolean hasPlayerAlreadyPlayedAdvancedCards) {
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
        advancedAlreadyPlayedThisTurn = hasPlayerAlreadyPlayedAdvancedCards;
    }

    public List<ColorCharacter> getEntrance() {
        return entrance;
    }

    public List<ColorCharacter> getTeachers() {
        return teachers;
    }

    public int[] getTables() {
        return tables;
    }

    public int getTowers() {
        return towers;
    }

    public ColorTower getTowerColor() {
        return towerColor;
    }

    private String addStudentEntrance(int index){
        return "\t" + (entrance.size() > index ? GameBoard.getColorString(entrance.get(index)) + Printable.STUDENT : " ") +
                Printable.TEXT_RESET;
    }

    private String addTower(int index){
        return "\t" + (towers > index ? GameBoard.getColorTowerString(towerColor) + Printable.TOWER : " ") + Printable.TEXT_RESET;
    }

    private String addTable(int index){
        StringBuilder temp = new StringBuilder();
        temp.append("\t");
        for(int i = 0; i < 10; i++){
            temp.append(tables[index] > i ? GameBoard.getColorString(ColorCharacter.values()[index]) + Printable.STUDENT : " ").append(Printable.TEXT_RESET);
            temp.append("\t");
        }
        temp.append(teachers.contains(ColorCharacter.values()[index]) ? GameBoard.getColorString(ColorCharacter.values()[index]) + Printable.TEACHER : " ").append(Printable.TEXT_RESET);
        temp.append("\t").append(" ").append(Printable.V_BAR);
        return temp.toString();
    }

    public boolean isAdvancedAlreadyPlayedThisTurn() {
        return advancedAlreadyPlayedThisTurn;
    }

    @Override
    public String toString() {
        StringBuilder school = new StringBuilder();
        int studentIndex = 0, towerIndex = 0;
        school.append(Printable.ML_CORNER).append(Printable.H_BAR.repeat(3*4 - 1))
                .append(Printable.T_BAR).append(Printable.H_BAR.repeat(Printable.LENGTH - 24))
                .append(Printable.T_BAR).append(Printable.H_BAR.repeat(3*4 - 2))
                .append(Printable.MR_CORNER).append("\n");
        for(int i = 0; i < tables.length; i++){
            school.append(Printable.V_BAR);
            school.append(addStudentEntrance(studentIndex++));
            school.append(addStudentEntrance(studentIndex++));
            school.append("\t").append(Printable.V_BAR);

            school.append(addTable(i));

            school.append(addTower(towerIndex++));
            school.append(addTower(towerIndex++));
            school.append("\t").append(Printable.V_BAR);

            school.append("\n");
        }
        school.append(Printable.BL_CORNER).append(Printable.H_BAR.repeat(3*4 - 1))
                .append(Printable.TR_BAR).append(Printable.H_BAR.repeat(Printable.LENGTH - 24))
                .append(Printable.TR_BAR).append(Printable.H_BAR.repeat(3*4 - 2))
                .append(Printable.BR_CORNER).append("\n");

        return school.toString();
    }
}
