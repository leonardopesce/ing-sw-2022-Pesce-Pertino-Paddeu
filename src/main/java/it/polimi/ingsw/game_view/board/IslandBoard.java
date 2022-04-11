package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IslandBoard implements Serializable {
    List<ColorCharacter> students;
    int towerNumber;
    ColorTower towerColor;
    int deniedCounter;

    public IslandBoard(Island island) {
        students = new ArrayList<>(island.getStudents().stream().map(BasicCharacter::getColor).toList());
        towerNumber = island.getTowers().size();
        //TODO check if there are more than 0 tower
        towerColor = island.getTowers().size() > 0 ? island.getTowers().get(0).getColor() : ColorTower.WHITE;
        deniedCounter = island.getIsBlocked().get();
    }
}
