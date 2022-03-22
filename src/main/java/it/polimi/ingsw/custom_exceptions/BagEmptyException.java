package it.polimi.ingsw.custom_exceptions;

public class BagEmptyException extends Exception{
    public BagEmptyException(){super();}

    public BagEmptyException(String s) {
        super(s);
    }
}
