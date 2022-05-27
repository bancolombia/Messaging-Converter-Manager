package co.com.bancolombia.models;

import lombok.*;

import java.util.TreeMap;

@Builder
@Data
@Getter
@NoArgsConstructor
public class TemplateTransactionPlain extends TreeMap<String, TemplateTransactionPlain.ResourceTemplate> {

    @Builder
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

    }


}
