package it.polimi.ingsw.custom_exceptions;

public class EmptyDiningTableException extends Exception{
    public EmptyDiningTableException() {
        super();
    }

    public EmptyDiningTableException(String s) {super(s);}
}
