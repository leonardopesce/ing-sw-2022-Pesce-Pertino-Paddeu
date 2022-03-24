package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.basic.Student;
import it.polimi.ingsw.game_model.character.character_utils.AdvancedCharacterType;

import java.util.List;

/**
 * A class to represent a specific type of Character Card: Monk, Jester, Princess, since they share a similar effect.
 * They all contain a specific number of students and let the player perform a specific action with them.
 *
 * @see AdvancedCharacter
 * @see AdvancedCharacterType
 */
public class StudentStorageAdvancedCharacter extends AdvancedCharacter{
    private List<Student> students;

    public StudentStorageAdvancedCharacter(AdvancedCharacterType type) {
        super(type, 3);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students){
        this.students.addAll(students);
    }
}
