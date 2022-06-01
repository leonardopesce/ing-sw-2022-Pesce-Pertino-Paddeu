package it.polimi.ingsw.custom_exceptions;

/**
 * Custom exception. Thrown when something tries to pick a student from an empty bag.
 */
public class BagEmptyException extends Exception{
    public BagEmptyException(){super();}

    /**
     * @param s the exception error message.
     */
    public BagEmptyException(String s) {
        super(s);
    }
}
