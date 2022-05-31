package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent islands in a light way, to transmit as fewer data as possible
 */
public class IslandBoard implements Serializable {
    private final List<ColorCharacter> students;
    private final int islandIndex;
    private final int towerNumber;
    private final ColorTower towerColor;
    private final int deniedCounter;
    private final boolean hasMotherNature;
    private final int ID;

    /**
     * Constructor method, create the island ring, current students on each island, current tower number,
     * island index, current color of towers built on the island(there can be only towers with the same color),
     * deniedCounter(a property of an island given by an advanced card), the presence of mother nature and island ID.
     * @param island current island
     * @param hasMotherNature flag to report if mother nature is on the island or not
     * @param islandIndex index of island
     *
     * @see Island
     * @see it.polimi.ingsw.game_model.character.MotherNature
     * @see it.polimi.ingsw.game_model.Game
     */
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
                Printable.V2_BAR + "   ID = " + islandIndex + (islandIndex < 10 ? "  " : " ") + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "  " + (deniedCounter > 0 ? Printable.DENY + deniedCounter : "  ") + "   " + (hasMotherNature ? Printable.TEXT_ORANGE + Printable.MOTHER_NATURE + Printable.TEXT_RESET + " " : "  ") + "  " + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "  " + Printable.TEXT_RED + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size() + (students.stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size() < 10 ? "   " : "  ") + Printable.TEXT_GREEN + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() + (students.stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() < 10 ? "  " : " ") + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "  " + Printable.TEXT_PURPLE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size() + (students.stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size() < 10 ? "   " : "  ") + Printable.TEXT_YELLOW + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() + (students.stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() < 10 ? "  " : " ") + Printable.V2_BAR + "\n" +
                Printable.V2_BAR + "  " + Printable.TEXT_BLUE + Printable.STUDENT + Printable.TEXT_RESET + students.stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size() + (students.stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size() < 10 ? "   " : "  ") + (towerNumber > 0 ? GameBoard.getColorTowerString(towerColor) + Printable.TOWER + Printable.TEXT_RESET
                + towerNumber + "  " : "    ") +
                Printable.V2_BAR + "\n" +
                Printable.BL2_CORNER + Printable.H2_BAR.repeat(11) + Printable.BR2_CORNER + "\n";
    }

    /**
     * Get the students placed on the island
     * @return list of students on the island
     */
    public List<ColorCharacter> getStudents() {
        return students;
    }

    /**
     * gets island ID
     * @return ID of island
     */
    public int getID(){
        return ID;
    }

    /**
     * get number of towers present on the island
     * @return number of same color towers present on the island
     */
    public int getTowerNumber() {
        return towerNumber;
    }

    /**
     * Get current tower's color set on the island
     * @return color of tower set on the island
     */
    public ColorTower getTowerColor() {
        return towerColor;
    }

    /**
     * get the presence of a "No Entry Tile"  on an island, that is a tile which prevents influence calculation for
     * one turn, it expires when Mother Nature step on a island once after a "No Entry Tile" is placed on the island.
     * @return deniedCounter, that is the flag that notify If the "No entry Tile" has already expired.
     */
    public int getDeniedCounter() {
        return deniedCounter;
    }

    /**
     * Get the information of the presence of mother nature on a island.
     * @return hasMotherNature flag which is true if Mother Nature is on the island, false otherwise.
     */
    public boolean hasMotherNature() {
        return hasMotherNature;
    }
}
