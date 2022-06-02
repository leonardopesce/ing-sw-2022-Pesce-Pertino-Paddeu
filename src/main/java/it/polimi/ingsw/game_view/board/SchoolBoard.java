package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 /**
 * Class representing the school board in in a light way to transmit fewer data as possible
 */
public class SchoolBoard implements Serializable {
    public static final long serialVersionUID = 1L;
    List<ColorCharacter> entrance = new ArrayList<>();
    List<ColorCharacter> teachers = new ArrayList<>();
    int[] tables = new int[5];
    int towers;
    ColorTower towerColor;
    boolean advancedAlreadyPlayedThisTurn = false;

    /**
     * Constructor class to set the school board
     * @param school is the board that contains entrance, dining hall, students and professors
     * @param colorTower tower color of player's board, one for each player
     * @param hasPlayerAlreadyPlayedAdvancedCards flag to report if the player has already played an advanced card,
     * since a player can play only one advanced card per turn.
     */
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

    /**
     * get the students on the entrance of the school board
     * @return students on the entrance of the school board, which are represented by a list
     */
    public List<ColorCharacter> getEntrance() {
        return entrance;
    }

    /**
     * get teachers on the school board
     * @return list of teachers on the school board
     */
    public List<ColorCharacter> getTeachers() {
        return teachers;
    }

    /**
     * get the tables on which are sat the students.
     *
     * @return The array of integers containing the students sitting in each dining hall: the index of the array
     * represents which color is the table(students can sit on a table only if they have the same color of the table),
     * the integer is the number of students sit in the dining hall.
     * For example if <code> array[0] == 6 </code> in the first table there are 6 students.
     */
    public int[] getTables() {
        return tables;
    }

    /**
     * Get towers on the school board
     * @return an integer representing the number of towers on the school board
     */
    public int getTowers() {
        return towers;
    }

    /**
     * get color tower assigned to the school board, (one for each player).
     * @return Color of the tower
     * @see ColorTower
     */
    public ColorTower getTowerColor() {
        return towerColor;
    }

    /**
     * given the index of student entrance, print the selected student entrance
     * @param index index of student entrance to print
     */
    private String addStudentEntrance(int index){
        return "   " + (entrance.size() > index ? GameBoard.getColorString(entrance.get(index)) + Printable.STUDENT : " ") +
                Printable.TEXT_RESET;
    }

    /**
     * given the index of student entrance, print the selected student entrance
     * @param index of tower to print
     * @return printed string
     */
    private String addTower(int index){
        return "  " + (towers > index ? GameBoard.getColorTowerString(towerColor) + Printable.TOWER : " ") + Printable.TEXT_RESET;
    }

    /**
     * given the index, print the corresponding table in the school board
     * @param index index of the school table to print
     * @return printed string of the school table
     */
    private String addTable(int index){
        StringBuilder temp = new StringBuilder();
        temp.append("   ");
        for(int i = 0; i < 10; i++){
            temp.append(tables[index] > i ? GameBoard.getColorString(ColorCharacter.values()[index]) + Printable.STUDENT : " ").append(Printable.TEXT_RESET);
            temp.append("   ");
        }
        temp.append(teachers.contains(ColorCharacter.values()[index]) ? GameBoard.getColorString(ColorCharacter.values()[index]) + Printable.TEACHER : " ").append(Printable.TEXT_RESET);
        temp.append("   ").append(" ").append(Printable.V_BAR);
        return temp.toString();
    }

    /**
     * get the value of flag that report is an advanced card has been played on this turn.
     * @return true if an advanced card has been played this turn, false otherwise.
     */
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
            school.append("   ").append(Printable.V_BAR);

            school.append(addTable(i));

            school.append(addTower(towerIndex++));
            school.append(addTower(towerIndex++));
            school.append("    ").append(Printable.V_BAR);

            school.append("\n");
        }
        school.append(Printable.BL_CORNER).append(Printable.H_BAR.repeat(3*4 - 1))
                .append(Printable.TR_BAR).append(Printable.H_BAR.repeat(Printable.LENGTH - 24))
                .append(Printable.TR_BAR).append(Printable.H_BAR.repeat(3*4 - 2))
                .append(Printable.BR_CORNER).append("\n");

        return school.toString();
    }
}
