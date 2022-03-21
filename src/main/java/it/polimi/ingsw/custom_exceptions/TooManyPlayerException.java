package it.polimi.ingsw.custom_exceptions;

public class TooManyPlayerException extends Exception{
    public TooManyPlayerException() { super(); }
    public TooManyPlayerException(String msg) { super(msg); }
}
