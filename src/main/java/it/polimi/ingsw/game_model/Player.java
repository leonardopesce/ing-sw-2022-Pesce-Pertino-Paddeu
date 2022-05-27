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

    public Player(String nickname, DeckType deckType) {
        this.nickname = nickname;
        this.deckAssistants = new DeckAssistants(deckType);
    }

    public void initialSetup(List<Student> students, int numTower, ColorTower color){
        school = new School(students, numTower);
        this.color = color;

    }

    public void setPlayedSpecialCard(){
        playedSpecialCard = true;
    }

    public boolean hasPlayedSpecialCard() {
        return playedSpecialCard;
    }

    public void resetPlayedSpecialCard(){
        playedSpecialCard = false;
    }

    public ColorTower getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }

    public void playAssistant(Assistant discardedCard) {
         this.discardedCard = deckAssistants.playAssistant(discardedCard);
    }

    public School getSchool() {
        return school;
    }

    public Assistant getDiscardedCard() {
        return discardedCard;
    }

    public int getTowersAvailable(){
        return school.getTowersAvailable();
    }

    public List<Tower> removeNTowers(int x){
        List<Tower> towers = new ArrayList<>();
        for(int i = 0; i < x && getTowersAvailable() > 0; i++){
            towers.add(new Tower(color));
            school.setTowersAvailable(getTowersAvailable() - 1);
        }
        return towers;
    }

    public void addNTowers(int x){
        school.setTowersAvailable(getTowersAvailable() + x);
    }

    public int getMoney() {
        return money;
    }

    public DiningTable getDiningTableWithColor(ColorCharacter color){
        DiningTable t = school.getDiningHall().getTables()[0];
        for(DiningTable table: school.getDiningHall().getTables()){
            if(table.getColor() == color){
                t = table;
            }
        }
        return t;
    }

    public boolean hasTeacherOfColor(ColorCharacter color){
        for(Teacher t: getTeachers()){
            if(t.getColor() == color){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfStudentAtTableOfColor(ColorCharacter color){
        return getDiningTableWithColor(color).getNumberOfStudents();
    }

    public Teacher moveTeacherOfColor(ColorCharacter color){
        for(Teacher t: getTeachers()){
            if(t.getColor() == color){
                getTeachers().remove(t);
                return t;
            }
        }

        return null;
    }

    public DeckAssistants getDeckAssistants() {
        return deckAssistants;
    }

    public List<Teacher> getTeachers(){
        return school.getDiningHall().getTeacherList();
    }

    public int getNumberOfMovedStudents() {
        return movedStudents;
    }

    public void incrementNumberOfMovedStudents(){
        movedStudents++;
    }

    public void resetNumberOfMovedStudents() {
        this.movedStudents = 0;
    }

    public void moveStudentToDiningHall(ColorCharacter color) throws TooManyStudentsException {
        school.getDiningHall().getTableOfColor(color).addStudent();
    }

    public int addMoney(int availableMoney){
        if(availableMoney > 0){
            availableMoney--;
            money++;
        }
        return availableMoney;
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

    public void setMoney(int money) {
        this.money = money;
    }
}
