package game_model;

import game_model.character.AdvancedCharacter;
import game_model.character.basic.Student;

import java.util.List;

public class Game {
    private List<Player> players;
    private int turn;
    private List<Student> unpickedStudents;
    private Terrain terrain;

    public Game() {
    }

    public void nextTurn(){
        turn++;
    }


}
