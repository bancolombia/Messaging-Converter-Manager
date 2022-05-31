package co.com.bancolombia.models.headers;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Device {
    private String id;
    private String agent;
}
