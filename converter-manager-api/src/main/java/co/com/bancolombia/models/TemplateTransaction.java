package co.com.bancolombia.models;

import co.com.bancolombia.api.TemplateValidations;
import lombok.*;

import java.util.TreeMap;

@Builder
@Data
@Getter
@NoArgsConstructor
public class TemplateTransaction extends TreeMap<String, TemplateTransaction.ResourceTemplate> {

    @Builder(toBuilder = true)
    @Data
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceTemplate {
        private String transactionName;
        private String channel;
        private String transaction;
        private String templateIn;
        private String templateOut;
        private String templateError;
        private TemplateValidations templateValidations;

    }


}
