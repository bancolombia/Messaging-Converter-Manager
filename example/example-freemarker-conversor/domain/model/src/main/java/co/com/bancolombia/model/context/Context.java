package co.com.bancolombia.model.context;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Context {
    private String id;
    private LocalDateTime dateTime;
    private String templateCode;
}
