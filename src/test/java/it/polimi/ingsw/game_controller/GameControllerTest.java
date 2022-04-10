package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GameControllerTest {
    Game game;
    GameController controller;

    @BeforeEach
    void initialization() {
        game = new GameExpertMode(2);
        controller = new GameController(game);
    }

    @DisplayName("Player Creation standard")
    @Test
    void playerCreationStandardNoErrorTest(){
        controller.createPlayer("Alberto", DeckType.KING);
        controller.createPlayer("Leonardo", DeckType.ELDER);
        Assertions.assertEquals("Alberto", game.getPlayers().get(0).getNickname());
        Assertions.assertEquals("Leonardo", game.getPlayers().get(1).getNickname());
        Assertions.assertEquals(2, game.getNumberOfPlayers());
    }

    @DisplayName("Player creation with duplicate nicknames")
    @Test
    void duplicateNicknameExceptionTest(){
        controller.createPlayer("Paolo", DeckType.KING);
        Assertions.assertThrows(NicknameAlreadyChosenException.class,() -> controller.createPlayer("Paolo", DeckType.ELDER));
    }

    @DisplayName("Player creation with duplicate deck types")
    @Test
    void duplicateDeckTypesCreationTest(){
        controller.createPlayer("Paolo", DeckType.KING);
        controller.createPlayer("Alberto", DeckType.KING);
        Assertions.assertEquals(1, game.getNumberOfPlayers());
    }

    @DisplayName("Play an assistant card")
    @Test
    void playAnAssistantCard(){
        controller.createPlayer("Paolo", DeckType.KING);
        controller.createPlayer("Alberto", DeckType.SORCERER);


        var firstPlayer = controller.getCurrentPlayer().getNickname();
        var assistantType = controller.getCurrentPlayer().getDeckAssistants().getAssistants().get(0);

        controller.selectAssistantCard(firstPlayer, 0);
        Assertions.assertEquals(assistantType, controller.getCurrentPlayer().getDiscardedCard());

    }


}