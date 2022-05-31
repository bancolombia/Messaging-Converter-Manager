package co.com.bancolombia.exceptions;


import co.com.bancolombia.models.ErrorConverse;

public class ConverseException extends Exception {

    public ConverseException(String message) {
        super(message);
    }
    public ConverseException(Throwable cause,String message) {
        super(message,cause);
    }

    public ConverseException( ErrorConverse errorMessage) {
        super(errorMessage.getReason());
    }

}
