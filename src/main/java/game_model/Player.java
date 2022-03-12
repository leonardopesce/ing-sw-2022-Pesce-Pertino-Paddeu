package game_model;

import game_model.character.Assistant;
import game_model.character.DeckAssistants;
import game_model.school.School;

import java.util.List;

public class Player {
    private final int id;
    private int color;
    private School school = new School();
    private DeckAssistants deckAssistants;
    private Assistant discardedCard;
    private int money = 0;

    public Player(int id) {
        this.id = id;
    }

    public void discardAssistant(){

    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public School getSchool() {
        return school;
    }

    public Assistant getDiscardedCard() {
        return discardedCard;
    }

    public void setDiscardedCard(Assistant discardedCard) {
        this.discardedCard = discardedCard;
    }

    public int getMoney() {
        return money;
    }
}
