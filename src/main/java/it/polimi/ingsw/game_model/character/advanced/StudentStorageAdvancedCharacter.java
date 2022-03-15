package it.polimi.ingsw.game_model.character.advanced;

import it.polimi.ingsw.game_model.character.basic.Student;

import java.util.List;

public class StudentStorageAdvancedCharacter extends AdvancedCharacter{
    private List<Student> students;

    public StudentStorageAdvancedCharacter(AdvancedCharacterType type) {
        super(type);
    }


}
