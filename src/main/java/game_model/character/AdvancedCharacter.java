package game_model.character;

public class AdvancedCharacter extends Character{
    private AdvancedCharacter instance;

    public AdvancedCharacter(){
        super();
    }

    public AdvancedCharacter getInstance() {
        if(instance == null){
            instance = new AdvancedCharacter();
        }
        return instance;
    }

    //effetto viene
}