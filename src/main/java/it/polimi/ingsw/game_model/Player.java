package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.DeckAssistants;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
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

    public Player(String nickname) {
        this.nickname = nickname;
        this.school = new School();
    }

    public Player(String nickname, Player mate){
        this.nickname = nickname;
        // le torri del player saranno le stesse del mate
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

    public void resetPlayedSpecialCard(){
        playedSpecialCard = false;
    }

    public ColorTower getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }

    public void playAssistant() {
        //TODO control based event, una volta ricevuto dal controller quale carta ha scelto il giocatore
        // la togliamo dal suo deck e la impostiamo come quella appena giocata (in un turno due giocatori
        // non possono giocare la stessa carta)
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

    public List<Teacher> getTeachers(){
        return school.getDiningHall().getTeacherList();
    }

}
