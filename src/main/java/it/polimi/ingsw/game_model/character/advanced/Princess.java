package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;

import java.util.ArrayList;
import java.util.List;

public class Princess extends AdvancedCharacter{
    private List<Student> studentsOnCard;

    public Princess(Game game){
        super(AdvancedCharacterType.PRINCESS, game);

        studentsOnCard = new ArrayList<>();
        try {
            studentsOnCard.addAll(game.getBag().drawNStudentFromBag(4));
        } catch (BagEmptyException e) {
            // Impossible to reach since the cards are eventually setup at the beginning of the match
            e.printStackTrace();
        }
    }

    public List<Student> getStudentsOnCard() {
        return studentsOnCard;
    }

    /**
     * Take 1 student from this card and place it in your dining room. Then, draw a new student
     * from the bag and place it on this card.
     * @param player who played the card effect
     * @param studentFromCard selected student
     */
    public void playEffect(Player player, int studentFromCard){
        player.getSchool().getDiningHall().getTableOfColor(studentsOnCard.get(studentFromCard).getColor()).addStudent();
        studentsOnCard.remove(studentFromCard);
        try {
            studentsOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            e.printStackTrace();
        }
    }

}
