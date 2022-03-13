package game_model.school;

public class DiningTable {
    private int color;
    private int numberOfStudents = 0;
    private final int MAX_POSITIONS = 10;

    public DiningTable(int color) {
        this.color = color;
    }

    public void addStudent(){
        numberOfStudents++;
    }

    public int getColor() {
        return color;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }


}
