package it.polimi.ingsw.game_model.character;

import it.polimi.ingsw.custom_exceptions.IslandNotPresentException;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

public class MotherNature extends Character {
    private int position;

    public MotherNature(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void moveOfIslands(Terrain terrain, int x){
        Island currentIsland = terrain.getIslands().get(0);
        try {
            currentIsland = terrain.getIslandWithId(position);
        }
        catch (IslandNotPresentException e){
            e.printStackTrace();
        }

        while(x > 0){
            currentIsland = terrain.getNextIsland(currentIsland);
            x--;
        }
        position = currentIsland.getId();
    }
}