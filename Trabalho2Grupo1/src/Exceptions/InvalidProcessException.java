package Exceptions;

public class InvalidProcessException extends Exception {
    public InvalidProcessException(String errorMessage){
        super(errorMessage);
    }
}
