package it.polimi.ingsw.custom_exceptions;

/**
 * Custom exception. Thrown when something tries to add a student to a full {@link it.polimi.ingsw.game_model.school.DiningTable}.
 */
public class TooManyStudentsException extends Exception{
    public TooManyStudentsException() {
        super();
    }

    /**
     * @param s the exception error message.
     */
    public TooManyStudentsException(String s) {
        super(s);
    }
}
