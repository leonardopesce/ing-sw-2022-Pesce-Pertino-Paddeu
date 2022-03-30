package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.game_type.Game;

import java.util.ArrayList;
import java.util.List;

public class Princess extends AdvancedCharacter{
    private List<Student> studensOnCard;

    public Princess(Game game){
        super(AdvancedCharacterType.PRINCESS, game);

        studensOnCard = new ArrayList<>();
        try {
            studensOnCard.addAll(game.getBag().drawNStudentFromBag(4));
        } catch (BagEmptyException e) {
            // Impossible to reach since the cards are eventually setup at the beginning of the match
            e.printStackTrace();
        }
    }


    public void playEffect(Player player, int studentFromCard){
        player.getSchool().getDiningHall().getTableOfColor(studensOnCard.get(studentFromCard).getColor()).addStudent();
        studensOnCard.remove(studentFromCard);
        try {
            studensOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            e.printStackTrace();
        }
    }
}
