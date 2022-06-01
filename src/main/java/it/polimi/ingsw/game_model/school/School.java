package it.polimi.ingsw.game_model.school;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;

import java.util.List;

/**
 * Class to manage the  player's full game board
 */
public class School {
    private final DiningHall diningHall = new DiningHall();
    private final Entrance entrance = new Entrance();
    private Integer towersAvailable;

    /**
     * @param students a list of students to add to the entrance.
     * @param towersAvailable the number of towers to set when the <code>School</code> object gets created.
     *
     * @see Student
     * @see it.polimi.ingsw.game_model.character.basic.Tower
     */
    public School(List<Student> students, int towersAvailable){
        entrance.addAllStudents(students);
        this.towersAvailable = towersAvailable;
    }

    /**
     * Returns the number of towers which haven't been built on an island yet.
     * @return the number of towers which haven't been built on an island yet.
     */
    public int getTowersAvailable(){
        return towersAvailable;
    }

    /**
     * Sets the given number of towers in the entrance.
     * @param towersAvailable the number of towers to set into the entrance.
     */
    public void setTowersAvailable(int towersAvailable) {
        this.towersAvailable = towersAvailable;
    }

    /**
     * Returns the entrance object.
     * @return the entrance object.
     */
    public Entrance getEntrance() {
        return entrance;
    }

    /**
     * Returns the {@link DiningHall} of this School.
     * @return the {@link DiningHall} of this School.
     *
     * @see DiningHall
     */
    public DiningHall getDiningHall() {
        return diningHall;
    }

    /**
     * Add teacher on player's game board
     * @param t teacher to add
     *
     * @see Teacher
     */
    public void addTeacher(Teacher t){
        diningHall.getTeacherList().add(t);
    }
}
