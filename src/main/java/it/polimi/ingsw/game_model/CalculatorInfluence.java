package it.polimi.ingsw.game_model;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.world.Island;

public class CalculatorInfluence {
    Player player;
    Island island;

    public CalculatorInfluence(Player player, Island island){
        this.player = player;
        this.island = island;
    }

    public int evaluate(){
        return playerTowerInfluence() + playerStudentInfluence();
    }

    /**
     * Given a player and an island of the board, returns the influence given by the control of any tower on that island.
     * <p>
     *     For example, if <code>pl</code> equals the white player, and the <code>island</code> has size equals to 1 with
     *     a White Tower built on it, then the method returns 1. <br>
     *     Otherwise, if the island is bigger (aggregation of 2 or
     *     more islands), it means that there are more than one tower to be counted (according to our implementation choices,
     *     exactly the size of the island).
     *
     * @return the additional influence brought by towers on a specific island for a specific playe
     */
    protected int playerTowerInfluence(){
        if(!island.getTowers().isEmpty() && island.getTowers().get(0).getColor() == player.getColor()){
            return island.getTowers().size();
        }
        return 0;
    }

    /**
     * Given a player and an island of the board, calculate the influence brought by the students on the island itself.
     *
     * <p>
     *     A student of a specific color, counts as 1 during the influence calculation for the player who owns the
     *     corresponding professor. <br>
     *     This method returns the influence the specified player has by counting, according to the rule mentioned before,
     *     the sum of all the students whose corresponding professors are owned by that player.
     *
     * @return the total influence brought by the students whose professors are controlled by the specified player
     */
    protected int playerStudentInfluence(){
        int influence = 0;
        for(Teacher t: player.getTeachers()){
            for(Student s: island.getStudents()){
                if(t.getColor() == s.getColor()){
                    influence++;
                }
            }
        }
        return influence;
    }

}
