package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.advanced.Postman;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.GamePhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class GameControllerTest {
    Game game;
    GameExpertMode gameExpert;
    GameController controller;
    private final String[] names = {"Leonardo", "Paolo", "Alberto", "Bot"};

    void initialization(int numOfPlayer, boolean expertMode) {
        if(expertMode){
            gameExpert = new GameExpertMode(numOfPlayer);
            game = gameExpert;
        }
        else {
            game = new Game(numOfPlayer);
        }
        controller = new GameController(game);
        for(int i = 0; i < numOfPlayer; i++){
            controller.createPlayer(names[i], controller.getAvailableDeckType().get(0));
        }
    }

    @DisplayName("Player Creation standard")
    @Test
    void playerCreationStandardNoErrorTest(){
        initialization(2, false);

        Assertions.assertEquals("Leonardo", game.getPlayers().get(0).getNickname());
        Assertions.assertEquals("Paolo", game.getPlayers().get(1).getNickname());
        Assertions.assertEquals(2, game.getNumberOfPlayers());
    }

    @DisplayName("Play an assistant card")
    @Test
    void playAnAssistantCardTest(){
        initialization(2, false);

        var firstPlayer = controller.getCurrentPlayer().getNickname();
        var assistantType = controller.getCurrentPlayer().getDeckAssistants().getAssistants().get(0);

        new PlayAssistantCardAction(firstPlayer, 0).perform(controller);
        Assertions.assertEquals(assistantType.getType(), game.getPlayers().stream().reduce((pl1, pl2) -> pl1.getNickname().equals(firstPlayer) ? pl1 : pl2).get().getDiscardedCard().getType());

    }

    @DisplayName("Play planning phase and starts action phase")
    @Test
    void playPlanningPhaseAndStartsActionPhaseTest(){
        initialization(2, false);

        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        Assertions.assertEquals(GamePhase.ACTION_PHASE_MOVING_STUDENTS, game.getGamePhase());

    }

    @DisplayName("Already played assistant error test")
    @Test
    void invalidAssistantPlayedBecauseAlreadyPlayedByAnotherPlayerTest(){
        initialization(3,true);

        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        var playerWhichMakesTheInvalidMove = controller.getCurrentPlayer();
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);

        Assertions.assertNull(playerWhichMakesTheInvalidMove.getDiscardedCard());
    }

    @DisplayName("Moving 3 students to the dining hall test")
    @Test
    void actionPhaseByMovingStudentsToTheDiningHallTest() {
        initialization(2, false);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        for(int i=0;i<2;i++) {
            for(int j=0;j<3;j++) {
                new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), j).perform(controller);
            }

            new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(),1).perform(controller);
            new ChooseCloudCardAction(controller.getCurrentPlayer().getNickname(), i).perform(controller);
        }

        Assertions.assertEquals(GamePhase.PLANNING_PHASE, game.getGamePhase());
    }

    @DisplayName("Play advanced card test")
    @Test
    void playAdvancedCardTest() throws Exception{
        initialization(2, true);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        new PlayAdvancedCardAction(controller.getCurrentPlayer().getNickname(), new Postman(game), controller.getCurrentPlayer()).perform(controller);

        Assertions.assertTrue(controller.getCurrentPlayer().hasPlayedSpecialCard());
        Assertions.assertEquals(controller.getCurrentPlayer().getDiscardedCard().getPossibleSteps(), controller.getCurrentPlayer().getDiscardedCard().getType().getPossibleSteps() + 2);
    }

    @DisplayName("Move student to island")
    @Test
    void moveStudentToIslandTest() {
        initialization(2, true);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        var islandOldSize = game.getTerrain().getIslands().get(0).getStudents().size();

        new MoveStudentToIslandAction(controller.getCurrentPlayer().getNickname(), 0, 0).perform(controller);

        Assertions.assertEquals(islandOldSize, game.getTerrain().getIslands().get(0).getStudents().size()-1);
    }

    @DisplayName("Mixed student movements (entrance to hall | entrance to islands)")
    @Test
    void mixedStudentsMovementsTest() {
        initialization(2, true);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        var island0OldSize = game.getTerrain().getIslands().get(0).getStudents().size();
        var island2OldSize = game.getTerrain().getIslands().get(2).getStudents().size();
        var studentMovedToDiningHallColor = controller.getCurrentPlayer().getSchool().getEntrance().getStudent(1).getColor();
        var oldTableOfColorSize = controller.getCurrentPlayer().getSchool().getDiningHall().getTableOfColor(studentMovedToDiningHallColor).getNumberOfStudents();

        new MoveStudentToIslandAction(controller.getCurrentPlayer().getNickname(), 0, 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToIslandAction(controller.getCurrentPlayer().getNickname(), 2, 2).perform(controller);

        Assertions.assertEquals(island0OldSize, game.getTerrain().getIslands().get(0).getStudents().size()-1);
        Assertions.assertEquals(island2OldSize, game.getTerrain().getIslands().get(2).getStudents().size()-1);
        Assertions.assertEquals(oldTableOfColorSize, controller.getCurrentPlayer().getSchool().getDiningHall().getTableOfColor(studentMovedToDiningHallColor).getNumberOfStudents() - 1);
        Assertions.assertEquals(GamePhase.ACTION_PHASE_MOVING_MOTHER_NATURE, game.getGamePhase());
    }

    @DisplayName("Build a communication message")
    @Test
    void communicationMessageBuildTest() {
        initialization(2, true);

        var newMessage = new CommunicationMessage(CommunicationMessage.MessageType.HELP, "Test message");

        Assertions.assertEquals(CommunicationMessage.MessageType.HELP, newMessage.getID());
        Assertions.assertEquals("Test message", newMessage.getMessage());
    }

    @DisplayName("Refill clouds with empty bag test")
    @Test
    void refillCloudsWithEmptyBagTest() throws Exception{
        initialization(2, false);

        while(!game.getBag().isEmpty()) game.getBag().drawStudentFromBag();
        var list = new ArrayList<Student>();
        list.add(new Student(ColorCharacter.GREEN));
        game.getBag().insertBack(list);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        for(int i=0;i<2;i++) {
            for(int j=0;j<3;j++) {
                new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), j).perform(controller);
            }

            new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(),1).perform(controller);
            new ChooseCloudCardAction(controller.getCurrentPlayer().getNickname(), i).perform(controller);
        }

        // Checking the emptiness of clouds (due to lack of students in the bag)
        Assertions.assertEquals(0, game.getTerrain().getCloudCards().get(0).getStudent().size());
        Assertions.assertEquals(0, game.getTerrain().getCloudCards().get(0).getStudent().size());
    }
}