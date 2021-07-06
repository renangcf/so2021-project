package Exceptions;

public class NoSuchMemoryException extends Exception {
    public NoSuchMemoryException(String errorMessage){
        super(errorMessage);
    }
}
