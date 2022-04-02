package it.polimi.ingsw.game_model;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.custom_exceptions.NicknameAlreadyChosenException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_controller.GameController;
import it.polimi.ingsw.game_model.character.advanced.AdvancedCharacter;
import it.polimi.ingsw.game_model.character.advanced.Bard;
import it.polimi.ingsw.game_model.character.advanced.Bartender;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.basic.Teacher;
import it.polimi.ingsw.game_model.character.basic.Tower;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.character.character_utils.DeckType;
import it.polimi.ingsw.game_model.utils.ColorCharacter;
import it.polimi.ingsw.game_model.utils.ColorTower;
import it.polimi.ingsw.game_model.world.Island;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    GameExpertMode gameExpert;
    GameController controller;
    private String[] names = {"Leonardo", "Paolo", "Alberto", "Bot"};

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
            try {
                controller.createPlayer(names[i], DeckType.values()[i]);
            } catch (NicknameAlreadyChosenException e) {
                e.printStackTrace();
            }
        }
    }


    @DisplayName("Setup board 2 players Normal mode")
    @Test
    void setUpBoard2PlayerNormalModeTest() throws BagEmptyException {
        int nPlayers = 2;
        boolean expertMode = false;
        initialization(nPlayers, expertMode);
        int n = 120 - game.INITIAL_NUMBER_OF_STUDENTS_TO_DRAW * nPlayers - game.NUMBER_OF_STUDENTS_ON_CLOUD * game.NUMBER_OF_CLOUDS;
        setupBoardTest(n, expertMode);
    }

    @DisplayName("Setup board 3 players Normal mode")
    @Test
    void setUpBoard3PlayerNormalModeTest() throws BagEmptyException {
        int nPlayers = 3;
        boolean expertMode = false;
        initialization(nPlayers, expertMode);
        int n = 120 - game.INITIAL_NUMBER_OF_STUDENTS_TO_DRAW * nPlayers - game.NUMBER_OF_STUDENTS_ON_CLOUD * game.NUMBER_OF_CLOUDS;
        setupBoardTest(n, expertMode);
    }

    @DisplayName("Setup board 4 players Normal mode")
    @Test
    void setUpBoard4PlayerNormalModeTest() throws BagEmptyException {
        int nPlayers = 4;
        boolean expertMode = false;
        initialization(nPlayers, expertMode);
        int n = 120 - game.INITIAL_NUMBER_OF_STUDENTS_TO_DRAW * nPlayers - game.NUMBER_OF_STUDENTS_ON_CLOUD * game.NUMBER_OF_CLOUDS;
        setupBoardTest(n, expertMode);
    }

    @DisplayName("Setup board 2 players Expert mode")
    @Test
    void setUpBoard2PlayerExpertModeTest() throws BagEmptyException {
        int nPlayers = 4;
        boolean expertMode = true;
        initialization(nPlayers, expertMode);
        int n = 120 - game.INITIAL_NUMBER_OF_STUDENTS_TO_DRAW * nPlayers - game.NUMBER_OF_STUDENTS_ON_CLOUD * game.NUMBER_OF_CLOUDS;
        n = removeStudentForSpecialCard(n);

        setupBoardTest(n, expertMode);
        for(Player player: game.players){
            assertEquals(1, player.getMoney());
        }
    }

    private int removeStudentForSpecialCard(int n){
        for(AdvancedCharacter character: game.terrain.getAdvancedCharacters()){
            if(character.getType() == AdvancedCharacterType.PRINCESS || character.getType() == AdvancedCharacterType.MONK){
                n -= 4;
            }
            else if(character.getType() == AdvancedCharacterType.JESTER){
                n -= 6;
            }
        }
        return n;
    }

    private void setupBoardTest(int n, boolean mode) throws BagEmptyException {
        List<Student> allStudents = new ArrayList<>();
        checkIslandSetUpTest();

        checkAndAddAllStudentsOnCloudTest(allStudents);
        checkAndAddAllStudentsOnPlayersEntrance(allStudents);


        checkBag(allStudents, n);
        if(!mode){
            Assertions.assertEquals(120, allStudents.size());
            checkAllStudentColor(allStudents);
        }
        else{
            Assertions.assertEquals(120, allStudents.size() - removeStudentForSpecialCard(0));
        }
    }

    private void checkAllStudentColor(List<Student> allStudents){
        int[] studentsColor = {0, 0, 0, 0, 0};
        for (Student student : allStudents) {
            studentsColor[student.getColor().ordinal()]++;
        }
        Assertions.assertArrayEquals(new int[]{24, 24, 24, 24, 24}, studentsColor);
    }

    private void checkBag(List<Student> allStudents, int numToDraw) throws BagEmptyException {
        assertTrue(allStudents.addAll(game.bag.drawNStudentFromBag(numToDraw)));
        Assertions.assertThrows(BagEmptyException.class, () -> game.bag.drawStudentFromBag());
    }

    private void checkAndAddAllStudentsOnCloudTest(List<Student> allStudents){
        //check if there are the exact number of students on each cloud
        for (int i = 0; i < game.getTerrain().getCloudCards().size(); i++) {
            allStudents.addAll(game.getTerrain().getCloudCards().get(i).removeStudentsOnCloud());
            Assertions.assertEquals(game.NUMBER_OF_STUDENTS_ON_CLOUD * (i + 1), allStudents.size());
        }
    }

    private void checkAndAddAllStudentsOnPlayersEntrance(List<Student> allStudents){
        for (Player pl : game.getPlayers()) {
            allStudents.addAll(pl.getSchool().getEntrance().getStudents());
            Assertions.assertEquals(game.INITIAL_NUMBER_OF_STUDENTS_TO_DRAW, pl.getSchool().getEntrance().getStudents().size());
        }
    }

    private void checkIslandSetUpTest(){
        //check if there are 12 island
        Assertions.assertEquals(12, game.getTerrain().getIslands().size());
        int[] islandsCheck = {0, 0, 0, 0, 0, 0, 0, 0};
        for (Island is : game.getTerrain().getIslands()) {
            //check if it has at most 1 student for island
            Assertions.assertFalse(is.getStudents().size() > 1);
            if (is.getStudents().size() == 0) {
                if (game.getMotherNature().getPosition() == is.getId()) {
                    islandsCheck[1]++;
                }
                else{
                    islandsCheck[2]++;
                }
            } else {
                islandsCheck[0]++;
                islandsCheck[is.getStudents().get(0).getColor().ordinal() + 3]++;
            }
        }
        // check if in total there are 10 students, 1 island has mother nature, 1 island is empty
        // and there are 2 player for color
        Assertions.assertArrayEquals(new int[]{10, 1, 1, 2, 2, 2, 2, 2}, islandsCheck);
    }


    @DisplayName("Basic teacher ownership")
    @Test
    void simpleUpdateTeacherOwnershipTest() throws TooManyStudentsException{
        initialization(2, false);

        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);
        game.updateTeacherOwnership(game.getPlayers().get(0), ColorCharacter.RED);
        Assertions.assertTrue(game.getPlayers().get(0).hasTeacherOfColor(ColorCharacter.RED));
    }

    @DisplayName("Steal teacher ownership")
    @Test
    void stealTeacherOwnershipTest() throws TooManyStudentsException{
        initialization(2, false);

        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.PINK);
        game.getPlayers().get(0).getSchool().addTeacher(new Teacher(ColorCharacter.PINK));
        game.moveStudentToDiningHall(game.getPlayers().get(1), ColorCharacter.PINK);
        game.moveStudentToDiningHall(game.getPlayers().get(1), ColorCharacter.PINK);
        game.updateTeacherOwnership(game.getPlayers().get(1), ColorCharacter.PINK);
        Assertions.assertTrue(game.getPlayers().get(1).hasTeacherOfColor(ColorCharacter.PINK));
        Assertions.assertFalse(game.getPlayers().get(0).hasTeacherOfColor(ColorCharacter.PINK));
    }

    @DisplayName("Draw teacher ownership condition normal game")
    @Test
    void drawTeacherOwnershipNormalTest() throws TooManyStudentsException{
        initialization(2, false);

        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.GREEN);
        game.getPlayers().get(0).getSchool().addTeacher(new Teacher(ColorCharacter.GREEN));
        game.moveStudentToDiningHall(game.getPlayers().get(1), ColorCharacter.GREEN);
        game.updateTeacherOwnership(game.getPlayers().get(0), ColorCharacter.GREEN);

        Assertions.assertTrue(game.getPlayers().get(0).hasTeacherOfColor(ColorCharacter.GREEN));
        Assertions.assertFalse(game.getPlayers().get(1).hasTeacherOfColor(ColorCharacter.GREEN));
    }

    @DisplayName("Draw teacher ownership condition bartender effect")
    @Test
    void drawTeacherOwnershipConditionBartenderEffectTest() throws TooManyStudentsException{
        initialization(2, true);

        var card = new Bartender(game);
        card.playEffect();

        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.GREEN);
        game.getPlayers().get(0).getSchool().addTeacher(new Teacher(ColorCharacter.GREEN));
        game.moveStudentToDiningHall(game.getPlayers().get(1), ColorCharacter.GREEN);
        game.updateTeacherOwnership(game.getPlayers().get(1), ColorCharacter.GREEN);

        Assertions.assertTrue(game.getPlayers().get(1).hasTeacherOfColor(ColorCharacter.GREEN));
        Assertions.assertFalse(game.getPlayers().get(0).hasTeacherOfColor(ColorCharacter.GREEN));
    }


    @DisplayName("evaluate influence with 2 player in a winning condition test")
    @Test
    void evaluateInfluences2PlayerWinningConditionTest() {
        initialization(2, false);

        var colorToCheck = game.terrain.getIslands().get((game.getMotherNature().getPosition()+1) % 12).getStudents().get(0).getColor();
        var oldTowersNum = game.getPlayers().get(0).getTowersAvailable();
        game.getPlayers().get(0).getSchool().addTeacher(new Teacher(colorToCheck));

        game.evaluateInfluences((game.getMotherNature().getPosition()+1) % 12);

        Assertions.assertEquals(oldTowersNum-1, game.getPlayers().get(0).getTowersAvailable());
        Assertions.assertEquals(game.getPlayers().get(0).getColor(), game.getTerrain().getIslands().get(game.getMotherNature().getPosition()+1).getTowers().get(0).getColor());
        Assertions.assertEquals(1, game.getTerrain().getIslands().get(game.getMotherNature().getPosition()+1 % 12).getTowers().size());
    }

    @DisplayName("evaluate influence with 2 player in a losing condition test")
    @Test
    void evaluateInfluences2PlayerLosingConditionTest() {
        initialization(2, false);

        var colorToCheck = game.terrain.getIslands().get((game.getMotherNature().getPosition()+1) % 12).getStudents().get(0).getColor();
        var oldTowersNum = game.getPlayers().get(0).getTowersAvailable();
        game.getPlayers().get(1).getSchool().addTeacher(new Teacher(colorToCheck));

        game.evaluateInfluences((game.getMotherNature().getPosition()+1) % 12);

        Assertions.assertEquals(oldTowersNum, game.getPlayers().get(0).getTowersAvailable());
        Assertions.assertEquals(oldTowersNum-1, game.getPlayers().get(1).getTowersAvailable());
        Assertions.assertNotEquals(game.getPlayers().get(0).getColor(), game.getTerrain().getIslands().get(game.getMotherNature().getPosition()+1%12).getTowers().get(0).getColor());
        Assertions.assertEquals(game.getPlayers().get(1).getColor(), game.getTerrain().getIslands().get(game.getMotherNature().getPosition()+1).getTowers().get(0).getColor());
        Assertions.assertEquals(1, game.getTerrain().getIslands().get(game.getMotherNature().getPosition()+1 % 12).getTowers().size());
    }

    @DisplayName("evaluate influence with 3 player in a drawish condition test")
    @Test
    void evaluateInfluences3PlayerDrawConditionTest() {
        initialization(3, false);

        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.GREEN));
        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.GREEN));
        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.RED));
        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.RED));
        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.PINK));
        game.getPlayers().get(0).getSchool().addTeacher(new Teacher(ColorCharacter.GREEN));
        game.getPlayers().get(1).getSchool().addTeacher(new Teacher(ColorCharacter.RED));
        game.getPlayers().get(2).getSchool().addTeacher(new Teacher(ColorCharacter.PINK));

        game.evaluateInfluences(game.getMotherNature().getPosition());

        Assertions.assertEquals(6, game.getPlayers().get(0).getTowersAvailable());
        Assertions.assertEquals(6, game.getPlayers().get(1).getTowersAvailable());
        Assertions.assertEquals(6, game.getPlayers().get(2).getTowersAvailable());
        Assertions.assertEquals(0, game.getTerrain().getIslands().get(game.getMotherNature().getPosition()).getTowers().size());
    }

    @DisplayName("player win when he ends the tower (2 player mode)")
    @Test
    void winner2PlayerByTowerTest() {
        initialization(2, false);
        game.terrain.getIslands().get(0).addAllTower(game.players.get(0).removeNTowers(game.players.get(0).getTowersAvailable()));
        assertEquals(1, game.winner().length);
        assertTrue(game.winner()[0].equals(names[0]));
    }

    @DisplayName("player win when he merges island (2 player mode)")
    @Test
    void winner2PlayerByMergingIslandTest() {
        initialization(2, false);

        Island is = game.terrain.getIslands().get(0);
        while(game.terrain.getIslands().size() > 3) {
            game.terrain.mergeIsland(is, game.getTerrain().getNextIsland(is));
        }
        is.addAllTower(game.players.get(1).removeNTowers(1));
        assertEquals(1, game.winner().length);
        assertTrue(game.winner()[0].equals(names[1]));

        // in case the players have the same number of tower check for the number of teacher
        game.terrain.getNextIsland(is).addAllTower(game.players.get(0).removeNTowers(1));
        game.players.get(0).getSchool().addTeacher(new Teacher(ColorCharacter.GREEN));
        assertEquals(1, game.winner().length);
        assertTrue(game.winner()[0].equals(names[0]));

        game.players.get(1).getSchool().addTeacher(new Teacher(ColorCharacter.RED));
        game.players.get(1).getSchool().addTeacher(new Teacher(ColorCharacter.PINK));
        assertEquals(1, game.winner().length);
        assertTrue(game.winner()[0].equals(names[1]));

        game.players.get(0).getSchool().addTeacher(new Teacher(ColorCharacter.YELLOW));
        assertEquals(2, game.winner().length);
        assertTrue(game.winner()[0].equals(names[0]) && game.winner()[1].equals(names[1]));
    }

    @DisplayName("Additional Money every 3 students placed")
    @Test
    void moneyIncrementAfterPlacing3StudentsOfTheSameColorInDiningHallTest() throws TooManyStudentsException {
        initialization(2, true);

        // Verifying correct init
        Assertions.assertEquals(1, game.getPlayers().get(0).getMoney());

        // Adding 3 students to the first player's dining hall --> should result also in adding
        // a coin to its treasury.
        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);
        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);
        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);

        // Verifying the additional coin
        Assertions.assertEquals(2, game.getPlayers().get(0).getMoney());
    }

    @DisplayName("Merge islands")
    @Test
    void mergeIslandTest() {
        initialization(2, false);

        var whiteTowers = new ArrayList<Tower>();
        whiteTowers.add(new Tower(ColorTower.WHITE));

        game.getTerrain().getNextIsland(game.getTerrain().getIslandWithId(game.getMotherNature().getPosition())).addAllTower(whiteTowers);
        game.getTerrain().getPreviousIsland(game.getTerrain().getIslandWithId(game.getMotherNature().getPosition())).addAllTower(whiteTowers);

        game.getPlayers().get(1).getSchool().addTeacher(new Teacher(ColorCharacter.RED));
        game.terrain.getIslands().get(game.getMotherNature().getPosition()).addStudent(new Student(ColorCharacter.RED));
        game.evaluateInfluences(game.getMotherNature().getPosition());

        // Now island should be merged

        Assertions.assertEquals(3, game.getTerrain().getIslandWithId(game.getMotherNature().getPosition()).getTowers().size());
        Assertions.assertEquals(ColorTower.WHITE, game.getTerrain().getIslandWithId(game.getMotherNature().getPosition()).getTowers().get(0).getColor());
    }

    @DisplayName("Bard effect")
    @Test
    void bardEffectTest() throws TooManyStudentsException{
        initialization(2, true);

        var card = new Bard(game);
        var fromEntranceToDiningRoom = new ArrayList<Integer>();
        var fromDiningRoomToEntrance = new ArrayList<ColorCharacter>();
        var colorFromEntrance = new ColorCharacter[3];
        colorFromEntrance[0] = game.getPlayers().get(0).getSchool().getEntrance().getStudents().get(0).getColor();
        colorFromEntrance[1] = game.getPlayers().get(0).getSchool().getEntrance().getStudents().get(1).getColor();
        colorFromEntrance[2] = game.getPlayers().get(0).getSchool().getEntrance().getStudents().get(2).getColor();
        fromEntranceToDiningRoom.add(0);
        fromEntranceToDiningRoom.add(1);
        fromEntranceToDiningRoom.add(2);
        fromDiningRoomToEntrance.add(ColorCharacter.RED);
        fromDiningRoomToEntrance.add(ColorCharacter.RED);
        fromDiningRoomToEntrance.add(ColorCharacter.RED);

        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);
        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);
        game.moveStudentToDiningHall(game.getPlayers().get(0), ColorCharacter.RED);

        card.playEffect(game.getPlayers().get(0), fromEntranceToDiningRoom, fromDiningRoomToEntrance);

        var redInEntranceChosen = 0;
        var blueInEntranceChosen = 0;
        var pinkInEntranceChosen = 0;
        var greenInEntranceChosen = 0;
        var yellowInEntranceChosen = 0;
        for(int i=0;i<3;i++) {
            switch (colorFromEntrance[i]) {
                case RED -> redInEntranceChosen++;
                case GREEN -> greenInEntranceChosen++;
                case PINK -> pinkInEntranceChosen++;
                case YELLOW -> yellowInEntranceChosen++;
                case BLUE -> blueInEntranceChosen++;
            }
        }

        Assertions.assertEquals(redInEntranceChosen, game.getPlayers().get(0).getSchool().getDiningHall().getTableOfColor(ColorCharacter.RED).getNumberOfStudents());
        Assertions.assertEquals(greenInEntranceChosen, game.getPlayers().get(0).getSchool().getDiningHall().getTableOfColor(ColorCharacter.GREEN).getNumberOfStudents());
        Assertions.assertEquals(yellowInEntranceChosen, game.getPlayers().get(0).getSchool().getDiningHall().getTableOfColor(ColorCharacter.YELLOW).getNumberOfStudents());
        Assertions.assertEquals(pinkInEntranceChosen, game.getPlayers().get(0).getSchool().getDiningHall().getTableOfColor(ColorCharacter.PINK).getNumberOfStudents());
        Assertions.assertEquals(blueInEntranceChosen, game.getPlayers().get(0).getSchool().getDiningHall().getTableOfColor(ColorCharacter.BLUE).getNumberOfStudents());
    }
}