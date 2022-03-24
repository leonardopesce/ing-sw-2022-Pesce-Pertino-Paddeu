package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.DeckAssistants;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;

import java.util.List;

public class Player {
    //TODO sistemare questione torri in 4 giocatori
    private final String nickname;
    private ColorTower color;
    private School school;
    private DeckAssistants deckAssistants;
    private Assistant discardedCard;
    private int money = 0;
    private boolean playedSpecialCard = false;
    private int movedStudents = 0;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public void initialSetup(List<Student> students, int numTower, ColorTower color){
        school = new School(students, numTower);
        this.color = color;
    }



    public void discardAssistant(){

    }

    public void playedSpecialCard(AdvancedCharacter card){
        if(!playedSpecialCard && money >= card.getAdvanceCharacterType().getCardCost()){
            money -= card.getAdvanceCharacterType().getCardCost();
            playedSpecialCard = true;
        }

    }

    public boolean hasPlayedSpecialCard() {
        return playedSpecialCard;
    }

    //TODO to use when GameControllerAdvanced
    public void resetPlayedSpecialCard(){
        playedSpecialCard = false;
    }

    public ColorTower getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }

    public void playAssistant(int x) {
        discardedCard = deckAssistants.playAssistant(x);
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

    public void removeNTowers(int x){
        if (x > getTowersAvailable()) {
            school.setTowersAvailable(0);
        } else {
            school.setTowersAvailable(getTowersAvailable() - x);
        }
    }

    public void addNTowers(int x){
        school.setTowersAvailable(getTowersAvailable() + x);
    }

    public void setDiscardedCard(Assistant discardedCard) {
        this.discardedCard = discardedCard;
    }

    public int getMoney() {
        return money;
    }

    public DiningTable getDiningTableWithColor(ColorCharacter color){
        //TODO sistemare con un eccezione
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

    public Teacher getTeacherOfColor(ColorCharacter color){
        for(Teacher t: getTeachers()){
            if(t.getColor() == color){
                getTeachers().remove(t);
                return t;
            }
        }
        // condition is never verified if you
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

    public void resetNumberOfMovedStudents() {
        this.movedStudents = 0;
    }

    public void moveStudentToDiningHall(ColorCharacter color) {
        try {
            school.getDiningHall().getTableOfColor(color).addStudent();
        } catch (TooManyStudentsException e) {
            e.printStackTrace();
        }
    }

    public boolean addMoney(Integer availableMoney){
        if(availableMoney > 0){
            availableMoney--;
            money++;
            return true;
        }
        return false;
    }
}
