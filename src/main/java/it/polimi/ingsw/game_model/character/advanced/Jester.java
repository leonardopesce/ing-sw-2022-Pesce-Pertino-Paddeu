package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.school.Entrance;
import it.polimi.ingsw.game_model.world.Island;
import it.polimi.ingsw.network.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Jester extends AdvancedCharacter{
    private final List<Student> studentsOnCard;

    public Jester(Game game){
        super(AdvancedCharacterType.JESTER, game);
        this.studentsOnCard = new ArrayList<>();

        // Setting up 6 students on the card
        try {
            studentsOnCard.addAll(game.getBag().drawNStudentFromBag(6));
        } catch (BagEmptyException e) {
            // Impossible to reach since the cards are eventually setup at the beginning of the match
            Logger.ERROR("Unable to setup the Jester card.", e.getMessage());
        }
    }

    public List<Student> getStudentsOnCard() {
        return studentsOnCard;
    }

    /**
     * You may take up to 3 students from this card and replace them with the same number of students from your Entrance.
     * @param attributes
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)){
            return false;
        }

        String playerNickname = (String) attributes[0];
        Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);
        ArrayList<Integer> studentsFromCard = (ArrayList<Integer>) attributes[1];
        ArrayList<Integer> studentsFromEntrance = (ArrayList<Integer>) attributes[2];

        Entrance playerEntrance = player.getSchool().getEntrance();

        for(int i=0; i<studentsFromCard.size(); i++) {
            studentsOnCard.add(studentsFromCard.get(i) + 1, playerEntrance.moveStudent(studentsFromEntrance.get(i)));
            playerEntrance.getStudents().add(studentsFromEntrance.get(i), studentsOnCard.remove(studentsFromCard.get(i).intValue()));
        }
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != this.getType().getArgsLength()){
            return false;
        }
        try {
            String playerNickname = (String) attributes[0];
            ArrayList<Integer> studentsFromCard = (ArrayList<Integer>) attributes[1];
            ArrayList<Integer> studentsFromEntrance = (ArrayList<Integer>) attributes[2];

            if(studentsFromCard.size() != studentsFromEntrance.size()) {
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
