package co.com.bancolombia.models.headers;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Context {
    private String id;
    private String sessionTracker;
    private LocalDateTime dateTime;
    private String transactionCode;
    private Customer customer;
}
