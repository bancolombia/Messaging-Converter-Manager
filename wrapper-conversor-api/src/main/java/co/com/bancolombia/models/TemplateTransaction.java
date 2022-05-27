package co.com.bancolombia.models;


import freemarker.template.Template;
import lombok.*;

import java.util.TreeMap;

@Builder
@Data
@Getter
@NoArgsConstructor
public class TemplateTransaction extends TreeMap<String, TemplateTransaction.ResourceTemplate> {

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

    }


}


