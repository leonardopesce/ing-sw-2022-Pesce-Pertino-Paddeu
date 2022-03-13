package game_model.character;

public class Assistant extends Character{
    private int value;
    private int motherNatureTurn;

    public Assistant(int value, int motherNatureTurn) {
        this.value = value;
        this.motherNatureTurn = motherNatureTurn;
    }

    public int getValue() {
        return value;
    }

    public int getMotherNatureTurn() {
        return motherNatureTurn;
    }
}
