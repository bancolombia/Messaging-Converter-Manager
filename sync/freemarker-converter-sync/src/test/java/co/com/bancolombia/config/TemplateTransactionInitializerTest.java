package co.com.bancolombia.config;

import co.com.bancolombia.api.TemplateValidations;
import co.com.bancolombia.commons.config.Config;
import co.com.bancolombia.commons.config.FreeMarkerConfig;
import co.com.bancolombia.models.TemplateTransaction;
import co.com.bancolombia.models.TemplateTransactionFreemarker;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Configuration
class TemplateTransactionInitializerTest {
    TemplateTransactionInitializer initializer = new TemplateTransactionInitializer();

    @Test
    void templateTransactionFreemarker() {
        TemplateValidations templateValidations = new TemplateValidations() {
            private static final String STATUS = "status";
            private static final String CODE = "code";
            private static final String OK_CODE = "200";

            @Override
            public boolean isOkResponse(Map<?, ?> response) {
                return ((Map<?, ?>) response.get(STATUS)).get(CODE).equals(OK_CODE);
            }
        };
        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder().transactionName("todo tasks").templateValidations(templateValidations).channel("channel test").transaction("100").templateIn("template in").templateOut("template out").templateError("template error").build();
        templateTransaction.put("100", resourceTemplate);
        templateTransaction.put("200", resourceTemplate);
        Config config = new Config();
        FreeMarkerConfig freeMarkerConfig = config.freeMarkerConfig();
        TemplateTransactionFreemarker transactionFreemarker = initializer.templateTransactionFreemarker(templateTransaction, freeMarkerConfig);
        assertEquals(2, transactionFreemarker.size());
    }


}
