package co.com.bancolombia.models;

import co.com.bancolombia.models.headers.Context;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessMessage {
    private Context context;
    private String body;
}
