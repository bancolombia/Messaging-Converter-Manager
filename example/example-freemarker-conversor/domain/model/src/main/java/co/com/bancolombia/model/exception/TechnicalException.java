package co.com.bancolombia.model.exception;

import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum errorMessage;

    public TechnicalException(Throwable cause, TechnicalExceptionEnum errorMessage) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }

}