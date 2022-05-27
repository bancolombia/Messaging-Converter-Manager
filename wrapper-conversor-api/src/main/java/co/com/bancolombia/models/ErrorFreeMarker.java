package co.com.bancolombia.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorFreeMarker  {
    private String error;
    private String reason;
    private String domain;
    private String code;
}