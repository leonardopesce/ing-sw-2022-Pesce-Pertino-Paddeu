package it.polimi.ingsw.game_model.world;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;

import java.util.List;

public class Island {
    private final int id;
    private int size = 1;
    private List<Student> students;
    private List<Tower> towers;

    public Island(int id) {
        this.id = id;
    }

    public void evaluateIsland(List<Player> players){
        Player mostInfluencer = players.stream().max((pl1, pl2) -> Math.max(playerInfluence(pl1), playerInfluence(pl2))).get();
        //adds back tower to owner player
        if(!towers.isEmpty()){
            for(Player pl: players){
                if(pl.getColor() == towers.get(0).getColor()){
                    pl.addNTowers(towers.size());
                    towers.clear();
                }
            }
        }
        //removes towers from most influencer player
        mostInfluencer.removeNTowers(size);
        for(int i = 0; i < size; i++){
            towers.add(new Tower(mostInfluencer.getColor()));
        }
    }

    public void incrementSize() { this.size++; }

    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
    }

    public void addAllStudent(List<Student> students) {
        this.students.addAll(students);
    }

    public void addAllTower(List<Tower> towers){
        this.towers.addAll(towers);
    }

    public List<Tower> getTowers() {
        return towers;
    }

    private int playerInfluence(Player pl){
        return playerTowerInfluence(pl) + playerStudentInfluence(pl);
    }

    public int getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }

    // influences given to player by towers
    private int playerTowerInfluence(Player pl){
        if(!towers.isEmpty() && towers.get(0).getColor() == pl.getColor()){
            return towers.size();
        }
        return 0;
    }

    // influences given to player by students on the island
    private int playerStudentInfluence(Player pl){
        int influence = 0;
        for(Teacher t: pl.getTeachers()){
            for(Student s: students){
                if(t.getColor() == s.getColor()){
                    influence++;
                }
            }
        }
        return influence;
    }
}
