package co.com.bancolombia.exceptions;


import co.com.bancolombia.models.ErrorConverse;
import lombok.Getter;

@Getter
public class ConverseException extends Exception {
    private  String errorCode;
    public ConverseException(Throwable cause,String message) {
        super(message,cause);
    }

    public ConverseException( ErrorConverse errorMessage) {
        super(errorMessage.getReason());
        this.errorCode = errorMessage.getCode();
    }

}
