package co.com.bancolombia.exceptions;


import co.com.bancolombia.models.ErrorConverse;
import lombok.Getter;

@Getter
public class ConverseException extends Exception {
    private final String errorCode;
    public ConverseException(Throwable cause,String message) {
        super(message,cause);
        this.errorCode = message;
    }

    public ConverseException( ErrorConverse errorMessage) {
        super(errorMessage.getReason());
        this.errorCode = errorMessage.getCode();
    }

}
