package it.polimi.ingsw.game_model.game_type;

public class Game2Player extends Game {

    @Override
    void actionPhase() {
        for(int i = 0; i < players.size() && winner() == -1; i++){
            //players.get(i).playActionPhase(terrain, );
        }
    }

    @Override
    void refillClouds() {

    }


}
