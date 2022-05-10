package it.polimi.ingsw.game_model.utils;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.world.Island;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class used to calculate the player's influence on an island: influence
 * is an integer which depends on towers on islands and students on
 * island whose professor is in the player's game board.
 */
public class CalculatorInfluence {

    public CalculatorInfluence(){
        // No need to build anything. There are no parameters to init either.
    }

    /**
     * Sum tower influence and student influence of the player
     * @param player player associated with influence
     * @param island island where to calculate influence
     * @return influence
     */
    public int evaluateForPlayer(Player player, Island island){
        return playerTowerInfluence(player, island) + playerStudentInfluence(player, island);
    }

    /**
     * If the game's rules include teams of players this function evaluates team influence: at first it creates teams
     * from the list of players; for each team(a list of
     * players with the same color) it evaluates the team influence which will be the sum of each
     * team member's influence; then it compares each team's influence, in case it founds a most influencing team,
     * return the list of players of the most influencing team; in case of a draw(no team has a unique maximal value of
     * influence), it frees the current island and returns an empty player list.
     * @param players list of players in the game
     * @param island island where to calculate influence
     * @return list of player if not draw, else an empty players list
     */
    public Optional<Player> evaluate(List<Player> players, Island island){
        // from the list of the player with the same color get the first player of the list with most influencing color
        if(!island.isBlocked()) {
            List<List<Player>> teams = getListsOfPlayersWithSameColor(players);
            List<Player> mostInfluencingTeam = teams.get(0);
            teams.remove(0);
            boolean draw = false;

            for (List<Player> team : teams) {
                if (mostInfluencingTeam.stream().mapToInt(pl -> evaluateForPlayer(pl, island)).sum() <
                        team.stream().mapToInt(pl -> evaluateForPlayer(pl, island)).sum()) {
                    mostInfluencingTeam = team;
                    draw = false;
                } else if (mostInfluencingTeam.stream().mapToInt(pl -> evaluateForPlayer(pl, island)).sum() ==
                        team.stream().mapToInt(pl -> evaluateForPlayer(pl, island)).sum()) {
                    draw = true;
                }
            }
            return Optional.ofNullable(draw ? null : mostInfluencingTeam.get(0));
        }
        else {
            island.freeIsland();
            return Optional.empty();
        }
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
     * @return the additional influence brought by towers on a specific island for a specific player
     */
    protected int playerTowerInfluence(Player player, Island island){
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
     *     corresponding teachers. <br>
     *     This method returns the influence the specified player has by counting, according to the rule mentioned before,
     *     the sum of all the students whose corresponding teacher are owned by that player.
     *
     * @return the total influence brought by the students whose teachers are controlled by the specified player
     */

    protected int playerStudentInfluence(Player player, Island island){
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


    /**
     * Given a list of players, for each player in the list creates a group of players with the same tower color
     * @param players list of total players
     * @return a list of players with the same tower color
     */
    private List<List<Player>> getListsOfPlayersWithSameColor(List<Player> players){
        List<List<Player>> tmp = new ArrayList<>();
        for(Player player: players){
            boolean notAddedFlag = true;
            for (List<Player> playerList : tmp) {
                if (playerList.get(0).getColor() == player.getColor()) {
                    playerList.add(player);
                    notAddedFlag = false;
                }
            }
            if(notAddedFlag){
                tmp.add(new ArrayList<>());
                tmp.get(tmp.size() - 1).add(player);
            }
        }
        return tmp;
    }

}
