package it.polimi.ingsw.game_model.character.advanced.PropostaDiPaolo;

import it.polimi.ingsw.game_model.CalculatorInfluence;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.game_type.Game;
import it.polimi.ingsw.game_model.world.Island;

public class Centaurus {
    private final Game game;

    public Centaurus(Game game) {
        this.game = game;
    }
    public void playEffect() {
        game.setInfluenceCalculator(
                new CalculatorInfluence(){
                    @Override
                    public int evaluate(Player player, Island island){
                        return playerStudentInfluence(player,island);
                    }
                }
        );
    }
}
