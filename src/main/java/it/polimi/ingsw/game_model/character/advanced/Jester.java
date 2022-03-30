package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;

import java.util.ArrayList;
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
            e.printStackTrace();
        }
    }


    public void playEffect(Player player, List<Integer> studentsFromCard, List<Integer> studentsFromEntrance){
        var playerEntrance = player.getSchool().getEntrance();

        if(studentsFromCard.size() == studentsFromEntrance.size()) {
            for(int i=0; i<studentsFromCard.size(); i++) {
                var tempStudentFromEntrance = playerEntrance.moveStudent(studentsFromEntrance.get(i));
                playerEntrance.addStudent(studentsOnCard.remove(studentsFromCard.get(i).intValue()));
                studentsOnCard.add(tempStudentFromEntrance);
            }
        }
    }
}
