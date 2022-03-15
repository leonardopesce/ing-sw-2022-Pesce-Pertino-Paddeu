package it.polimi.ingsw.custom_exceptions;

public class IslandNotPresentException extends Exception{
    public IslandNotPresentException(String s) {
        super(s);
    }
}
