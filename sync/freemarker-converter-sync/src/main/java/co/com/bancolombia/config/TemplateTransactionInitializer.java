package co.com.bancolombia.config;

import co.com.bancolombia.commons.config.FreeMarkerConfig;
import co.com.bancolombia.models.TemplateTransaction;
import co.com.bancolombia.models.TemplateTransactionFreemarker;
import freemarker.template.Template;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

@Configuration
@Log4j2
public class TemplateTransactionInitializer {

    @Bean
    public TemplateTransactionFreemarker templateTransactionFreemarker(TemplateTransaction templateTransaction, FreeMarkerConfig freeMarkerConfig) {
        TemplateTransactionFreemarker templateTransactionFreemarker = new TemplateTransactionFreemarker();
        templateTransaction.forEach((templateCode, resourceTemplate) -> {
            try {
                templateTransactionFreemarker.put(templateCode, TemplateTransactionFreemarker.ResourceTemplate.builder()
                        .transaction(resourceTemplate.getTransaction())
                        .channel(resourceTemplate.getChannel())
                        .transactionName(resourceTemplate.getTransactionName())
                        .templateJsonToXml(new Template(UUID.randomUUID().toString(), new StringReader(resourceTemplate.getTemplateJsonToXml()), freeMarkerConfig))
                        .templateJsonToXmlError(new Template(UUID.randomUUID().toString(), new StringReader(resourceTemplate.getTemplateJsonToXmlError()), freeMarkerConfig))
                        .templateXmlToJson(new Template(UUID.randomUUID().toString(), new StringReader(resourceTemplate.getTemplateXmlToObject()), freeMarkerConfig))
                        .templateXmlToJsonError(new Template(UUID.randomUUID().toString(), new StringReader(resourceTemplate.getTemplateXmlToObjectError()), freeMarkerConfig))
                        .templateValidations(resourceTemplate.getTemplateValidations())
                        .build());
            } catch (IOException e) {
                log.warn("TemplateTransactionInitializer.templateTransactionFreemarker", e);
            }
        });
        return templateTransactionFreemarker;
    }
}
