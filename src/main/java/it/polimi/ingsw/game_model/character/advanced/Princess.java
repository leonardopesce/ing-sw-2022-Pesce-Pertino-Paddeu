package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.custom_exceptions.BagEmptyException;
import it.polimi.ingsw.custom_exceptions.TooManyStudentsException;
import it.polimi.ingsw.game_model.Player;
import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;
import it.polimi.ingsw.game_model.Game;
import it.polimi.ingsw.game_model.school.DiningTable;

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
     * @param attributes values of the card
     */
    @Override
    public boolean playEffect(Object... attributes){
        if(!validateArgs(attributes)) return false;

        String playerNickname = (String) attributes[0];
        Player player = game.getPlayers().stream().filter(pl -> pl.getNickname().equals(playerNickname)).toList().get(0);
        Integer studentFromCard = (Integer) attributes[1];

        try {
            game.moveStudentToDiningHall(player, studentsOnCard.get(studentFromCard).getColor());
        } catch (TooManyStudentsException e) {
            // Impossible to reach since we check if all the students can be added in validateArgs method.
            return false;
        }

        studentsOnCard.remove(studentFromCard.intValue());
        try {
            studentsOnCard.add(game.getBag().drawStudentFromBag());
        } catch (BagEmptyException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected boolean validateArgs(Object... attributes) {
        if(attributes.length != 2){
            return false;
        }
        try{
            String playerNickname = (String) attributes[0];
            Integer studentFromCard = (Integer) attributes[1];

        }catch (Exception e){
            return false;
        }

        return true;
    }
}
