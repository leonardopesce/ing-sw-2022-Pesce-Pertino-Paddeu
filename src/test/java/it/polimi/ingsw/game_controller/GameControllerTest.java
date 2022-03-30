package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.game_type.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GameControllerTest {
    Game game;
    GameController controller;

    @BeforeEach
    void initialization() {
        game = new Game(2);
        controller = new GameController(game);
    }

    @DisplayName("Player Creation standard")
    @Test
    void playerCreationStandardNoErrorTest() throws Exception{
        controller.createPlayer("Alberto", DeckType.KING);
        controller.createPlayer("Leonardo", DeckType.ELDER);
        Assertions.assertEquals("Alberto", game.getPlayers().get(0).getNickname());
        Assertions.assertEquals("Leonardo", game.getPlayers().get(1).getNickname());
        Assertions.assertEquals(2, game.getNumberOfPlayers());
    }

    @DisplayName("Player creation with duplicates")
    @Test
    void duplicateNicknameExceptionTest() throws Exception{
        controller.createPlayer("Paolo", DeckType.KING);
        Assertions.assertThrows(NicknameAlreadyChosenException.class,() -> controller.createPlayer("Paolo", DeckType.ELDER));
    }


}