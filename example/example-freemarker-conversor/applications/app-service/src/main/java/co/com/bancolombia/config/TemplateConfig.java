package co.com.bancolombia.config;

import co.com.bancolombia.api.TemplateValidations;
import co.com.bancolombia.models.TemplateTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.Map;


@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class TemplateConfig {

    @Bean
    public TemplateTransaction templateTransaction() {
        TemplateValidations templateValidations = new TemplateValidations() {
            private static final String STATUS = "status";
            private static final String CODE = "code";
            private static final String OK_CODE = "200";

            @Override
            public boolean isOkResponse(Map<?, ?> response) {
                return ((Map<?, ?>)  response.get(STATUS)).get(CODE).equals(OK_CODE);
            }
        };
        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder()
                .transactionName("todo tasks")
                .templateValidations(templateValidations)
                .channel("channel test")
                .transaction("100")
                .templateIn("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><number>123</number>")
                .templateOut("{\"task\":{\"id\": \"123\",\"description\": \"this is a description\", \"name\":\"title\"}}")
                .templateError("{\"reason\":\"401\",\"domain\":\"na\",\"code\":\"401\",\"message\":\"error message\"}")
                .build();
        templateTransaction.put("100", resourceTemplate);
        return templateTransaction;
    }
}

