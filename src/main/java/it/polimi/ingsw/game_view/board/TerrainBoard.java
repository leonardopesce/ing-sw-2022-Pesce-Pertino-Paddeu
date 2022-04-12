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
    List<List<ColorCharacter>> cloudCards = new ArrayList<>();
    List<IslandBoard> islands = new ArrayList<>();
    List<AdvancedCharacterType> advancedCard = new ArrayList<>();

    public TerrainBoard(Terrain terrain) {
        for(CloudCard cloud: terrain.getCloudCards()){
            cloudCards.add(new ArrayList<>(cloud.getStudent().stream().map(BasicCharacter::getColor).toList()));
        }
        for(Island island: terrain.getIslands()){
            islands.add(new IslandBoard(island));
        }
        for(AdvancedCharacter card: terrain.getAdvancedCharacters()){
            advancedCard.add(card.getType());
        }

    }

    public List<List<ColorCharacter>> getCloudCards() {
        return cloudCards;
    }

    public List<AdvancedCharacterType> getAdvancedCard() {
        return advancedCard;
    }

    public List<IslandBoard> getIslands() {
        return islands;
    }

    @Override
    public String toString() {
        StringBuilder terrain = new StringBuilder();

        return null;
    }
}
