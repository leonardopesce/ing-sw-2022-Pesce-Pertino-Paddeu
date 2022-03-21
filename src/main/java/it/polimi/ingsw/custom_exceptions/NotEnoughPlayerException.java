package it.polimi.ingsw.custom_exceptions;

public class NotEnoughPlayerException extends Exception{
    public NotEnoughPlayerException() { super(); }
    public NotEnoughPlayerException(String msg) { super(msg); }
}
