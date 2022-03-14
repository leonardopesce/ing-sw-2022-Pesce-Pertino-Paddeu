package it.polimi.ingsw.game_model.game_type;

import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.BagOfStudents;
import it.polimi.ingsw.game_model.world.Terrain;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    public List<Player> players;
    public int[] planningOrder, actionOrder;
    public BagOfStudents bag;
    public Terrain terrain;

    public Game() {
    }

    public final void start(){
        createPlanningOrder();
        while(winner() != -1){
            refillClouds();
            // every "turn" is divided in planning phase and action phase
            planningPhase();
            createActionPhaseOrder();
            actionPhase();
            createNextPlanningOrder();
        }
    }

    public int winner(){
        //implements a function that returns the player which won -1 otherwise
        return -1;
    }

    private void planningPhase(){
        for(int pl: planningOrder){
            players.get(pl).playAssistant();
        }
    }

    private void createPlanningOrder(){
        planningOrder[0] = (int) Math.round(Math.random());
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    private void createNextPlanningOrder(){
        planningOrder[0] = actionOrder[0];
        for(int i = 1; i < players.size(); i++){
            planningOrder[i] = (planningOrder[i-1] + 1) % players.size();
        }
    }

    private void createActionPhaseOrder(){
        List<Pair<Integer, Integer>> playerAndValue = new ArrayList<>();
        //cycle to create all position
        for(int i = 0; i < players.size(); i++){
            playerAndValue.add(new Pair<Integer, Integer>(i, players.get(i).getDiscardedCard().getValue()));
        }

        playerAndValue.sort((a,b) -> Math.max(a.getValue(), b.getValue()));

        for(int i = 0; i < players.size(); i++){
            actionOrder[i] = playerAndValue.get(i).getKey();
        }
    }

    abstract void actionPhase();
    abstract void refillClouds();
}
