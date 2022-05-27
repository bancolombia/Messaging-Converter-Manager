package co.com.bancolombia.models.headers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {
    private String identification;
    private String documentType;
    private String segment;
    private String nickname;
    private String channel;
    private Device device;
}
