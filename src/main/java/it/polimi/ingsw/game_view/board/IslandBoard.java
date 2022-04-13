package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IslandBoard implements Serializable {
    List<ColorCharacter> students;
    int islandId;
    int towerNumber;
    ColorTower towerColor;
    int deniedCounter;

    public IslandBoard(Island island) {
        students = new ArrayList<>(island.getStudents().stream().map(BasicCharacter::getColor).toList());
        towerNumber = island.getTowers().size();
        //TODO check if there are more than 0 tower
        towerColor = island.getTowers().size() > 0 ? island.getTowers().get(0).getColor() : ColorTower.WHITE;
        deniedCounter = island.getIsBlocked().get();
        islandId = island.getId();
    }
    @Override
    public String toString(){
        StringBuilder island = new StringBuilder();
        island.append(Printable.TL2_CORNER).append(Printable.H2_BAR.repeat(15)).append(Printable.TR2_CORNER).append("\n");
        island.append(Printable.V2_BAR).append("\tID = " + islandId + "\t\t").append(Printable.V2_BAR).append("\n");
        island.append(Printable.V2_BAR).append("\t").append(deniedCounter > 0 ? Printable.DENY + deniedCounter : "  ").append("\t")
            .append(towerNumber > 0 ? GameBoard.getColorTowerString(towerColor) + Printable.TOWER + Printable.TEXT_RESET
                    + towerNumber + "\t\t": "  \t\t").append(Printable.V2_BAR).append("\n");
        island.append(Printable.V2_BAR).append("\t").append(Printable.TEXT_RED + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size()).append("\t").append(Printable.TEXT_GREEN + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() + "\t\t").append(Printable.V2_BAR).append("\n");
        island.append(Printable.V2_BAR).append("\t").append(Printable.TEXT_PURPLE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size()).append("\t").append(Printable.TEXT_YELLOW + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() + "\t\t").append(Printable.V2_BAR).append("\n");
        island.append(Printable.V2_BAR).append("\t").append(Printable.TEXT_BLUE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size()).append("\t\t\t").append(Printable.V2_BAR).append("\n");
        island.append(Printable.BL2_CORNER).append(Printable.H2_BAR.repeat(15)).append(Printable.BR2_CORNER).append("\n");

        return island.toString();
    }
}
