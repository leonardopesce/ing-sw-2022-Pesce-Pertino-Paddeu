package it.polimi.ingsw.game_controller;

import it.polimi.ingsw.game_controller.action.*;
import it.polimi.ingsw.game_model.character.Assistant;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.Postman;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.character.character_utils.AssistantType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.GameExpertMode;
import it.polimi.ingsw.game_model.school.DiningTable;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.GamePhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;

class GameControllerTest {
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

        var action = new PlayAssistantCardAction(firstPlayer, 0);
        action.perform(controller);
        System.out.println(action);
        Assertions.assertEquals(assistantType.getType(), game.getPlayers().stream().reduce((pl1, pl2) -> pl1.getNickname().equals(firstPlayer) ? pl1 : pl2).get().getDiscardedCard().getType());

    }

    @DisplayName("Play assistant card in a wrong turn")
    @Test
    void playAnAssistantCardInWrongTurnTest() {
        initialization(4, false);

        var actualFirstPlayerNickname = controller.getCurrentPlayer();
        var notFirstPlayerNickname = game.getPlayers().stream().filter(pl -> !pl.getNickname().equals(controller.getCurrentPlayer().getNickname())).toList().get(0).getNickname();
        var action = new PlayAssistantCardAction(notFirstPlayerNickname, 0);
        action.perform(controller);

        Assertions.assertEquals(actualFirstPlayerNickname, controller.getCurrentPlayer());
        Assertions.assertEquals(GamePhase.PLANNING_PHASE, game.getGamePhase());
    }

    @DisplayName("Play planning phase and starts action phase")
    @Test
    void playPlanningPhaseAndStartsActionPhaseTest(){
        initialization(2, false);

        var action1 = new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0);
        action1.perform(controller);
        System.out.println(action1);
        var action2 = new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1);
        action2.perform(controller);
        System.out.println(action2);

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
                var action = new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), j);
                action.perform(controller);
                System.out.println(action);
            }

            var action1 = new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(),1);
            action1.perform(controller);
            System.out.println(action1);
            var action2 = new ChooseCloudCardAction(controller.getCurrentPlayer().getNickname(), i);
            action2.perform(controller);
            System.out.println(action2);
        }

        Assertions.assertEquals(GamePhase.PLANNING_PHASE, game.getGamePhase());
    }

    @DisplayName("Move student in another player's turn")
    @Test
    void moveStudentInOtherPlayerTurnTest() {
        initialization(2,false);

        // Making a correct planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 3).perform(controller);

        // Entering the action phase: the second player will try to move a student not in his turn.
        var notCurrentPlayer = game.getPlayers().stream().filter(pl -> !pl.getNickname().equals(controller.getCurrentPlayer().getNickname())).toList().get(0);

        // Trying to put the student both onto an island and dining table.
        int oldTablesSize = Arrays.stream(notCurrentPlayer.getSchool().getDiningHall().getTables()).map(DiningTable::getNumberOfStudents).reduce(0, Integer::sum);
        int oldIslandSize = game.getTerrain().getIslands().get(0).getStudents().size();
        new MoveStudentToDiningHallAction(notCurrentPlayer.getNickname(), 0).perform(controller);
        var action = new MoveStudentToIslandAction(notCurrentPlayer.getNickname(), 0, 0);
        System.out.println(action);
        action.perform(controller);
        // Verifying that the old sizes have not changed.
        Assertions.assertEquals(oldTablesSize, Arrays.stream(notCurrentPlayer.getSchool().getDiningHall().getTables()).map(DiningTable::getNumberOfStudents).reduce(0, Integer::sum));
        Assertions.assertEquals(oldIslandSize, game.getTerrain().getIslands().get(0).getStudents().size());
    }

    @DisplayName("Move student in a wrong phase (not in ACTION_PHASE_MOVE_STUDENT)")
    @Test
    void moveStudentInWrongPhaseTest() {
        initialization(2, false);

        // (1) Trying to move a student in planning phase -> Not allowed
        int oldTablesSize = Arrays.stream(controller.getCurrentPlayer().getSchool().getDiningHall().getTables()).map(DiningTable::getNumberOfStudents).reduce(0, Integer::sum);
        int oldIslandSize = game.getTerrain().getIslands().get(0).getStudents().size();

        new MoveStudentToIslandAction(controller.getCurrentPlayer().getNickname(), 0, 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        Assertions.assertEquals(oldIslandSize, game.getTerrain().getIslands().get(0).getStudents().size());
        Assertions.assertEquals(oldTablesSize, Arrays.stream(controller.getCurrentPlayer().getSchool().getDiningHall().getTables()).map(DiningTable::getNumberOfStudents).reduce(0, Integer::sum));
    }

    @DisplayName("Move students to a full table")
    @Test
    void moveStudentsToAFullTableTest() throws Exception{
        initialization(2, false);

        // Playing the assistant cards
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        // Filling all the current player's tables
        for(DiningTable table : controller.getCurrentPlayer().getSchool().getDiningHall().getTables()) {
            for(int i=0;i<DiningTable.MAX_POSITIONS;i++) table.addStudent();
        }

        // Trying to play the action to move a student to its dining table
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(),0).perform(controller);

        // Checking that all the tables have exactly 10 students.
        for(DiningTable table : controller.getCurrentPlayer().getSchool().getDiningHall().getTables()) {
            Assertions.assertEquals(DiningTable.MAX_POSITIONS, table.getNumberOfStudents());
        }
    }

    @DisplayName("Move mother nature in wrong turn")
    @Test
    void moveMotherNatureInWrongTurnTest() {
        initialization(2, false);

        // Playing the planning phase and the action phase (student movement)
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);

        // Entering in the action phase (mother nature)
        var notCurrentPlayerNickname = game.getPlayers().stream().filter(pl -> !pl.getNickname().equals(controller.getCurrentPlayer().getNickname())).toList().get(0).getNickname();
        var oldMotherNaturePose = game.getMotherNature().getPosition();
        new MoveMotherNatureAction(notCurrentPlayerNickname, 1).perform(controller);

        // Checking that mother nature has not moved.
        Assertions.assertEquals(oldMotherNaturePose, game.getMotherNature().getPosition());
    }

    @DisplayName("Moving mother nature of an invalid amount of steps")
    @Test
    void moveMotherNatureOfAnInvalidAmountOfStepsTest() {
        initialization(2, false);

        // Playing the planning phase and the action phase (student movement)
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveStudentToDiningHallAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);

        // In this situation the current player can move mother nature of 1 step. We try making her move of 0 steps and 2 steps
        // which are both invalid moves.
        var oldMotherNaturePose = game.getMotherNature().getPosition();
        new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(), 2).perform(controller);
        // Checking that mother nature has not moved.
        Assertions.assertEquals(oldMotherNaturePose, game.getMotherNature().getPosition());
    }

    @DisplayName("Moving mother nature in a wrong game phase (not in ACTION_PHASE_MOVING_MOTHER_NATURE)")
    @Test
    void moveMotherNatureInWrongGamePhaseTest() {
        initialization(2, false);

        var oldMotherNaturePose = game.getMotherNature().getPosition();
        // We try to perform the action in ACTION_PHASE_MOVING_STUDENT
        game.setUpGamePhase(GamePhase.ACTION_PHASE_MOVING_STUDENTS);
        new MoveMotherNatureAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);
        Assertions.assertEquals(oldMotherNaturePose, game.getMotherNature().getPosition());
    }

    @DisplayName("Pick an empty cloud card")
    @Test
    void pickEmptyCloudCardTest() {
        initialization(2, false);

        // Playing the planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        // Setting up the game phase to the correct one and removing all the students from the cloud 0.
        game.setUpGamePhase(GamePhase.ACTION_PHASE_CHOOSING_CLOUD);
        game.getTerrain().getCloudCards().get(0).clear();
        var oldSecondIslandSize = game.getTerrain().getCloudCards().get(1).getStudent().size();

        new ChooseCloudCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        // Checking that the cloud cards remain unchanged
        Assertions.assertEquals(0, game.getTerrain().getCloudCards().get(0).getStudent().size());
        Assertions.assertEquals(oldSecondIslandSize, game.getTerrain().getCloudCards().get(1).getStudent().size());
    }

    @DisplayName("Pick a cloud card in a wrong game phase (not in ACTION_PHASE_CHOSING_CLOUD)")
    @Test
    void pickCloudInWrongGamePhaseTest() {
        initialization(2, false);

        // Playing the planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        // Setting up the game phase to an invalid one.
        game.setUpGamePhase(GamePhase.ACTION_PHASE_MOVING_MOTHER_NATURE);
        // Trying to pick the students from the cloud.
        var oldCloudSize = game.getTerrain().getCloudCards().get(0).getStudent().size();
        new ChooseCloudCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);

        // Checking that the cloud card has not been picked
        Assertions.assertEquals(oldCloudSize, game.getTerrain().getCloudCards().get(0).getStudent().size());
    }

    @DisplayName("Pick a cloud card in a wrong turn")
    @Test
    void pickCloudCardInWrongTurnTest() {
        initialization(2, false);

        // Playing the planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        // Setting up the game phase to the correct one.
        game.setUpGamePhase(GamePhase.ACTION_PHASE_CHOOSING_CLOUD);
        var oldCloudSize = game.getTerrain().getCloudCards().get(0).getStudent().size();
        var notCurrentPlayerNickname = game.getPlayers().stream().filter(pl -> !pl.getNickname().equals(controller.getCurrentPlayer().getNickname())).toList().get(0).getNickname();
        new ChooseCloudCardAction(notCurrentPlayerNickname, 0).perform(controller);

        // Checking that the island has not been picked.
        Assertions.assertEquals(oldCloudSize, game.getTerrain().getCloudCards().get(0).getStudent().size());
    }

    @DisplayName("Play advanced card test")
    @Test
    void playAdvancedCardTest() throws Exception{
        do {
            initialization(2, true);
        } while(game.getTerrain().getAdvancedCharacters().stream().map(AdvancedCharacter::getType).filter(type -> type.equals(AdvancedCharacterType.POSTMAN)).toList().size() == 0);

        // Simple planning phase
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 0).perform(controller);
        new PlayAssistantCardAction(controller.getCurrentPlayer().getNickname(), 1).perform(controller);

        var action = new PlayAdvancedCardAction(controller.getCurrentPlayer().getNickname(), AdvancedCharacterType.POSTMAN, controller.getCurrentPlayer().getNickname());
        System.out.println(action);
        System.out.println(GameAction.gameActionID.CHOOSE_CLOUD_CARD_ACTION);
        action.perform(controller);

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