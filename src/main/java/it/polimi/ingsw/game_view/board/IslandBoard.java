package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IslandBoard implements Serializable {
    private final List<ColorCharacter> students;
    private final int islandIndex;
    private final int towerNumber;
    private final ColorTower towerColor;
    private final int deniedCounter;
    private final boolean hasMotherNature;
    private final int ID;

    public IslandBoard(Island island, boolean hasMotherNature, int islandIndex) {
        students = new ArrayList<>(island.getStudents().stream().map(BasicCharacter::getColor).toList());
        towerNumber = island.getTowers().size();
        this.islandIndex = islandIndex;
        towerColor = island.getTowers().size() > 0 ? island.getTowers().get(0).getColor() : ColorTower.WHITE;
        deniedCounter = island.getIsBlocked().get();
        this.hasMotherNature = hasMotherNature;
        this.ID = island.getId();
    }
    @Override
    public String toString(){
        return Printable.TL2_CORNER + Printable.H2_BAR.repeat(11) + Printable.TR2_CORNER + "\n" +
                Printable.V2_BAR + "\tID = " + islandIndex + "\t" + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "\t" + (deniedCounter > 0 ? Printable.DENY + deniedCounter : "  ") + "\t" + (hasMotherNature ? Printable.TEXT_ORANGE + Printable.MOTHER_NATURE + Printable.TEXT_RESET : " ") + "\t" + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "\t" + Printable.TEXT_RED + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size() + "\t" + Printable.TEXT_GREEN + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() + "\t" + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "\t" + Printable.TEXT_PURPLE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size() + "\t" + Printable.TEXT_YELLOW + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() + "\t" + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "\t" + Printable.TEXT_BLUE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size() + "\t" + (towerNumber > 0 ? GameBoard.getColorTowerString(towerColor) + Printable.TOWER + Printable.TEXT_RESET
                + towerNumber + "\t" : "  \t") +
                Printable.V2_BAR + "\n" +
                Printable.BL2_CORNER + Printable.H2_BAR.repeat(11) + Printable.BR2_CORNER + "\n";
    }

    public List<ColorCharacter> getStudents() {
        return students;
    }

    public int getID(){
        return ID;
    }

    public int getIslandIndex() {
        return islandIndex;
    }

    public int getTowerNumber() {
        return towerNumber;
    }

    public ColorTower getTowerColor() {
        return towerColor;
    }

    public int getDeniedCounter() {
        return deniedCounter;
    }

    public boolean hasMotherNature() {
        return hasMotherNature;
    }
}
