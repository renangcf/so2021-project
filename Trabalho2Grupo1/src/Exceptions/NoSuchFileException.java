package Exceptions;

public class NoSuchFileException extends Exception {
    public NoSuchFileException(String errorMessage){
        super(errorMessage);
    }
}
