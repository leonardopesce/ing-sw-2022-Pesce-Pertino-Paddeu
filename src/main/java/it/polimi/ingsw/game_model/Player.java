package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.DeckAssistants;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.game_type.Game2Player;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.school.School;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nickName;
    private ColorTower color;
    private School school = new School();
    private DeckAssistants deckAssistants;
    private Assistant discardedCard;
    private int money = 0;

    public Player(String nickName) {
        this.nickName = nickName;
    }

    public void discardAssistant(){

    }

    public ColorTower getColor() {
        return color;
    }

    public String getNickName() {
        return nickName;
    }

    public void playAssistant() {

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

    public void moveStudents(List<Island> islands){
        int movedStudent = 0;
        while(movedStudent < Game2Player.TOTAL_MOVABLE_STUDENT){
            //TODO aggiungere la parte di spostamento degli studenti (event based)
            movedStudent++;
        }
    }



    public List<Teacher> getTeachers(){
        return school.getDiningHall().getTeacherList();
    }
}
