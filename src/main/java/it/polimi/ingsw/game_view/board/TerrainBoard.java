package it.polimi.ingsw.game_view.board;

import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.basic.BasicCharacter;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.world.CloudCard;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.game_model.world.Terrain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TerrainBoard implements Serializable {
    private final List<List<ColorCharacter>> cloudCards = new ArrayList<>();
    private final List<IslandBoard> islands = new ArrayList<>();
    private final List<AdvancedCardBoard> advancedCard = new ArrayList<>();

    public TerrainBoard(Terrain terrain, int motherNaturePosition) {
        for(CloudCard cloud: terrain.getCloudCards()){
            cloudCards.add(new ArrayList<>(cloud.getStudent().stream().map(BasicCharacter::getColor).toList()));
        }
        for(int i = 0; i < terrain.getIslands().size(); i++){
            islands.add(new IslandBoard(terrain.getIslands().get(i), terrain.getIslands().get(i).getId() == motherNaturePosition, i));
        }
        for(AdvancedCharacter card: terrain.getAdvancedCharacters()){
            advancedCard.add(new AdvancedCardBoard(card));
        }
    }

    public IslandBoard getIslandWithID(int ID){
        for (IslandBoard island : islands) {
            if (island.getID() == ID) {
                return island;
            }
        }
        return null;
    }

    public List<List<ColorCharacter>> getCloudCards() {
        return cloudCards;
    }

    public List<AdvancedCardBoard> getAdvancedCard() {
        return advancedCard;
    }

    public List<IslandBoard> getIslands() {
        return islands;
    }

    @Override
    public String toString() {
        StringBuilder terrain = new StringBuilder();
        terrain.append("ISLANDS\n");
        String[] islandString = new String[islands.size()];
        for(int i = 0; i < islands.size(); i++){
            islandString[i] = islands.get(i).toString();
        }
        for(int line = 0; line < islandString[0].chars().filter(c -> c == '\n').count(); line++){
            for (int i = 0; i < islandString.length / 2; i++) {
                terrain.append(islandString[i].split("\n")[line].replaceAll("\n", "    ")).append("    ");
            }
            terrain.append("\n");
        }

        for(int line = 0; line < islandString[0].chars().filter(c -> c == '\n').count(); line++){
            for (int i = islandString.length / 2; i < islandString.length; i++) {
                terrain.append(islandString[i].split("\n")[line].replaceAll("\n", "    ")).append("    ");
            }
            terrain.append("\n");
        }

        //CLOUD
        terrain.append("CLOUDS\n");
        String[] cloudsString = new String[cloudCards.size()];
        for(int i = 0; i < cloudCards.size(); i++){
            cloudsString[i] = getCloudCard(i);
        }

        for(int line = 0; line < cloudsString[0].chars().filter(c -> c == '\n').count(); line++){
            for (String s : cloudsString) {
                terrain.append(s.split("\n")[line].replaceAll("\n", "  ")).append("  ");
            }
            terrain.append("\n");
        }

        return terrain.toString();
    }


    private String getCloudCard(int id){
        return Printable.H3_BAR.repeat(26) + " \n" +
                Printable.V3_BAR + "        " + "CLOUD " + id + "          " + Printable.V3_BAR + "\n" +
                Printable.V3_BAR + "  " + Printable.TEXT_RED + Printable.STUDENT + Printable.TEXT_RESET + cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size() + (cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.RED)).toList().size() < 10 ? "  " : " ") +
                Printable.TEXT_GREEN + Printable.STUDENT + Printable.TEXT_RESET + cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() + (cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.GREEN)).toList().size() < 10 ? "  " : " ") +
                Printable.TEXT_BLUE + Printable.STUDENT + Printable.TEXT_RESET + cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size() + (cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.BLUE)).toList().size() < 10 ? "  " : " ") +
                Printable.TEXT_YELLOW + Printable.STUDENT + Printable.TEXT_RESET + cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() + (cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.YELLOW)).toList().size() < 10 ? "  " : " ") +
                Printable.TEXT_PURPLE + Printable.STUDENT + Printable.TEXT_RESET + cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size() + (cloudCards.get(id).stream().filter(color -> color.equals(ColorCharacter.PINK)).toList().size() < 10 ? "     " : "    ") + Printable.V3_BAR + "\n" +
                Printable.H3_BAR.repeat(26) + " \n";
    }
}
