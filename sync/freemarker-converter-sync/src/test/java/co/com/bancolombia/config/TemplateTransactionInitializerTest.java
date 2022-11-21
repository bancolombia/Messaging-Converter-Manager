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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Configuration
class TemplateTransactionInitializerTest {
    TemplateTransactionInitializer initializer = new TemplateTransactionInitializer();
    TemplateValidations templateValidations = new TemplateValidations() {
        @Override
        public boolean isOkResponseXmlToObject(Map<?, ?> object) {
            return true;
        }

        @Override
        public boolean isOkResponseJsonToXml(String json) {
            return true;
        }
    };

    @Test
    void templateTransactionFreemarker() {

        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder().transactionName("todo tasks").templateValidations(templateValidations).channel("channel test").transaction("100").templateJsonToXml("template in").templateJsonToXmlError("template json to xml error").templateXmlToObject("template out").templateXmlToObjectError("template xml to object error").build();
        templateTransaction.put("100", resourceTemplate);
        templateTransaction.put("200", resourceTemplate);
        Config config = new Config();
        FreeMarkerConfig freeMarkerConfig = config.freeMarkerConfig();
        TemplateTransactionFreemarker transactionFreemarker = initializer.templateTransactionFreemarker(templateTransaction, freeMarkerConfig);
        assertEquals(2, transactionFreemarker.size());
    }

    @Test
    void templateTransactionFreemarkerWithDeprecatedAttributes() {
        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder()
                .transactionName("todo tasks")
                .templateValidations(templateValidations)
                .channel("channel test")
                .transaction("100")
                .templateIn("template in")
                .templateOut("template out")
                .templateError("template xml to object error")
                .build();
        templateTransaction.put("100", resourceTemplate);
        templateTransaction.put("200", resourceTemplate);
        Config config = new Config();
        FreeMarkerConfig freeMarkerConfig = config.freeMarkerConfig();
        TemplateTransactionFreemarker transactionFreemarker = initializer.templateTransactionFreemarker(templateTransaction, freeMarkerConfig);
        assertEquals(2, transactionFreemarker.size());
    }

    @Test
    void shouldGenerateExceptionInTemplateTransactionFreemarker() {
        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder()
                .transactionName("todo tasks")
                .templateValidations(templateValidations)
                .channel("channel test")
                .transaction("100")
                .build();
        templateTransaction.put("100", resourceTemplate);
        Config config = new Config();
        FreeMarkerConfig freeMarkerConfig = config.freeMarkerConfig();
        assertThrows(NullPointerException.class, () -> initializer.templateTransactionFreemarker(templateTransaction, freeMarkerConfig));
    }

}
