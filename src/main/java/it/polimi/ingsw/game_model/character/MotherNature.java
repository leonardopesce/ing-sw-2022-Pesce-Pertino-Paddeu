package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

/**
 * MotherNature models the Mother nature piece in the game: its function is to determine the island where influence is
 * calculated: its main attributes are:
 * position: current island where mother nature is standing
 *

 */
public class MotherNature extends Character {
    private int position;

    public MotherNature(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Method to move mother nature on another island
     * @param terrain terrain where there are the islands
     * @param x number of steps to move mother nature
     */
    public void moveOfIslands(Terrain terrain, int x){
        Island currentIsland = terrain.getIslandWithId(position);

        while(x > 0){
            currentIsland = terrain.getNextIsland(currentIsland);
            x--;
        }
        position = currentIsland.getId();
    }
}