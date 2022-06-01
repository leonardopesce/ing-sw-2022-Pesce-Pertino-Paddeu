package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.DeckAssistants;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class to model the player in the game
 */
public class Player {
    private final String nickname;
    private ColorTower color;
    private School school;
    private final DeckAssistants deckAssistants;
    private Assistant discardedCard;
    private int money = 0;
    private boolean playedSpecialCard = false;
    private int movedStudents = 0;

    /**
     * @param nickname the player nickname.
     * @param deckType the player decktype.
     *
     * @see DeckType
     */
    public Player(String nickname, DeckType deckType) {
        this.nickname = nickname;
        this.deckAssistants = new DeckAssistants(deckType);
    }

    /**
     * Sets up the player board (school and towers' color).
     * @param students the students which have to be added to the entrance of the school board.
     * @param numTower the number of towers to set up.
     * @param color the towers' color.
     */
    public void initialSetup(List<Student> students, int numTower, ColorTower color){
        school = new School(students, numTower);
        this.color = color;

    }

    /**
     * Called when a character card is played, it updates the player state by setting to true the playedSpecialCard attribute.
     */
    public void setPlayedSpecialCard(){
        playedSpecialCard = true;
    }

    /**
     * Returns whether the player has already played a special card or not.
     * @return true if the player has already played a special card in this turn, otherwise false.
     */
    public boolean hasPlayedSpecialCard() {
        return playedSpecialCard;
    }

    /**
     * Called at the beginning of each turn, it resets the playedSpecialCard value to false.
     */
    public void resetPlayedSpecialCard(){
        playedSpecialCard = false;
    }

    /**
     * Returns the towers' color of the towers owned by the player.
     * @return the towers' color of the towers owned by the player.
     */
    public ColorTower getColor() {
        return color;
    }

    /**
     * Returns the player nickname.
     * @return the player nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Given an assistant card, it updates the player state by removing that card from the deck of the available ones and putting it as the last discarded card.
     * @param discardedCard the assistant card which has been played.
     */
    public void playAssistant(Assistant discardedCard) {
         this.discardedCard = deckAssistants.playAssistant(discardedCard);
    }

    /**
     * Returns the school board of the player.
     * @return the school board of the player.
     */
    public School getSchool() {
        return school;
    }

    /**
     * Returns the last assistant card played by the player.
     * @return the last assistant card played.
     */
    public Assistant getDiscardedCard() {
        return discardedCard;
    }

    /**
     * Returns the number of towers in the player school board (which still need to be built).
     * @return the number of towers in the player school board.
     */
    public int getTowersAvailable(){
        return school.getTowersAvailable();
    }

    /**
     * Removes and returns x towers from the school board.
     * @param x the number of towers which have to be remved and returned.
     * @return a list of x towers picked from the school board of the player.
     */
    public List<Tower> removeNTowers(int x){
        List<Tower> towers = new ArrayList<>();
        for(int i = 0; i < x && getTowersAvailable() > 0; i++){
            towers.add(new Tower(color));
            school.setTowersAvailable(getTowersAvailable() - 1);
        }
        return towers;
    }

    /**
     * Adds x towers to the school board of the player.
     * @param x the number of towers which have to be added to the school board of the player.
     */
    public void addNTowers(int x){
        school.setTowersAvailable(getTowersAvailable() + x);
    }

    /**
     * Returns the number of coin owned by the player.
     * @return the number of coin owned by the player.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Returns the dining table of the specified color.
     * @param color the color of the table which has to be returned.
     * @return the dining table of the specified color.
     *
     * @see DiningTable
     * @see ColorCharacter
     */
    public DiningTable getDiningTableWithColor(ColorCharacter color){
        DiningTable t = school.getDiningHall().getTables()[0];
        for(DiningTable table: school.getDiningHall().getTables()){
            if(table.getColor() == color){
                t = table;
            }
        }
        return t;
    }

    /**
     * Returns whether the player has the teacher of the specified color or not.
     * @param color the color of the teacher which has to be checked.
     * @return true if the player has the teacher of the specified color, otherwise false.
     *
     * @see Teacher
     * @see ColorCharacter
     */
    public boolean hasTeacherOfColor(ColorCharacter color){
        for(Teacher t: getTeachers()){
            if(t.getColor() == color){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of students contained in the table of the specified color.
     * @param color the color of the table to check.
     * @return the number of students contained in the table of the specified color.
     *
     * @see ColorCharacter
     * @see DiningTable
     */
    public int getNumberOfStudentAtTableOfColor(ColorCharacter color){
        return getDiningTableWithColor(color).getNumberOfStudents();
    }

    /**
     * Removes the teacher of the specified color from the player's school board.
     * @param color the color of the teacher which has to be removed.
     * @return the teacher removed from the player school board.
     *
     * @see Teacher
     * @see ColorCharacter
     */
    public Teacher moveTeacherOfColor(ColorCharacter color){
        for(Teacher t: getTeachers()){
            if(t.getColor() == color){
                getTeachers().remove(t);
                return t;
            }
        }

        return null;
    }

    /**
     * Returns the assistants deck of the player.
     * @return the assistants deck of the player.
     *
     * @see DeckAssistants
     */
    public DeckAssistants getDeckAssistants() {
        return deckAssistants;
    }

    /**
     * Returns a list of teacher owned by the player.
     * @return a list of teacher owned by the player.
     *
     * @see Teacher
     */
    public List<Teacher> getTeachers(){
        return school.getDiningHall().getTeacherList();
    }

    /**
     * Returns the number of students already moved by the player in the current turn (action phase).
     * @return the number of students already moved by the player in the current turn.
     */
    public int getNumberOfMovedStudents() {
        return movedStudents;
    }

    /**
     * Increment by 1 the number of students moved by the player in the current turn (action phase).
     */
    public void incrementNumberOfMovedStudents(){
        movedStudents++;
    }

    /**
     * Called at the beginning of each turn, it resets to 0 the number of students already moved in the current turn.
     */
    public void resetNumberOfMovedStudents() {
        this.movedStudents = 0;
    }

    /**
     * Adds a student to the dining table of the specified color.
     * @param color the color of the table in which a student has to be added.
     * @throws TooManyStudentsException if the dining table is already full.
     *
     * @see DiningTable
     * @see ColorCharacter
     * @see Student
     */
    public void moveStudentToDiningHall(ColorCharacter color) throws TooManyStudentsException {
        school.getDiningHall().getTableOfColor(color).addStudent();
    }

    /**
     * Adds the specified amount of coins to the player 'bank'.
     * @param availableMoney the number of coins which have to be added.
     * @return the new amount of coins of the player.
     */
    public int addMoney(int availableMoney){
        if(availableMoney > 0){
            availableMoney--;
            money++;
        }
        return availableMoney;
    }

    /**
     * Sets a specified amount of coins at the player.
     * @param money the number of coins which have to be set.
     */
    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return money == player.money && playedSpecialCard == player.playedSpecialCard && movedStudents == player.movedStudents && nickname.equals(player.nickname) && color == player.color && school.equals(player.school) && deckAssistants.equals(player.deckAssistants) && discardedCard.equals(player.discardedCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, color, school, deckAssistants, discardedCard, money, playedSpecialCard, movedStudents);
    }

}
