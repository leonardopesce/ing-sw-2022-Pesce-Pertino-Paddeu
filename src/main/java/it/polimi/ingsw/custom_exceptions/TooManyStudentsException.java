package it.polimi.ingsw.custom_exceptions;

public class TooManyStudentsException extends Exception{
    public TooManyStudentsException() {
        super();
    }

    public TooManyStudentsException(String s) {
        super(s);
    }
}
