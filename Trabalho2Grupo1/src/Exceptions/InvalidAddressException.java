package Exceptions;

public class InvalidAddressException extends Exception {
    public InvalidAddressException(String errorMessage){
        super(errorMessage);
    }
}
