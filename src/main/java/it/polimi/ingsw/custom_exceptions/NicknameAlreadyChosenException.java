package it.polimi.ingsw.custom_exceptions;

public class NicknameAlreadyChosenException extends Exception{
    public NicknameAlreadyChosenException() { super(); }
    public NicknameAlreadyChosenException(String s) { super(s); }
}
