package co.com.bancolombia.api.handlerexception.model.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponse {
    private String reason;
    private String domain;
    private String code;
    private String message;
}
