package co.com.bancolombia.models;


import co.com.bancolombia.api.TemplateValidations;
import freemarker.template.Template;
import lombok.*;

import java.util.TreeMap;

@Builder
@Data
@Getter
@NoArgsConstructor
public class TemplateTransactionFreemarker extends TreeMap<String, TemplateTransactionFreemarker.ResourceTemplate> {

    @Builder
    @Data
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceTemplate {
        private String transactionName;
        private String channel;
        private String transaction;
        private Template templateIn;
        private Template templateOut;
        private Template templateError;
        private TemplateValidations templateValidations;

    }


}


