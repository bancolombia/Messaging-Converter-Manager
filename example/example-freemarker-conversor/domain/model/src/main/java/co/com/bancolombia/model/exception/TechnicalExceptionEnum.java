package co.com.bancolombia.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {
    TECHNICAL_TEMPLATE_NOTFOUND("001", "An error occurred while processing Template Request"),
    UNKNOWN_ERROR("500", "Internal server error"),
    UNAUTHORIZED("401", "Unauthorized");


    private final String code;
    private final String message;

    public static TechnicalExceptionEnum getByError(String error) {
        for (TechnicalExceptionEnum err : values()) {
            if (err.code.contains(error)) {
                return err;
            }
        }
        return TechnicalExceptionEnum.UNKNOWN_ERROR;
    }
}
