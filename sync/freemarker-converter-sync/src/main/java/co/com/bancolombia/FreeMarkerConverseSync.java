package co.com.bancolombia;

import co.com.bancolombia.api.ConverseDataGateway;
import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.models.ErrorConverse;
import co.com.bancolombia.models.TemplateTransactionFreemarker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class FreeMarkerConverseSync implements ConverseDataGateway {

    private static final String BUSINESS_HEADER = "businessHeader";
    private static final String BUSINESS_BODY = "businessBody";
    private static final String BUSINESS_RESPONSE = "businessResponse";

    private final TemplateTransactionFreemarker templateTransaction;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper = new XmlMapper();


    @SneakyThrows
    @Override
    public String jsonToXml(String json, String templateCode, Object... context) {
        TemplateTransactionFreemarker.ResourceTemplate resourceTemplate = getResourceTemplate(templateCode);
        Template templateToUse = getTemplate(json, resourceTemplate);
        Map<String, Object> root = new HashMap<>();
        if (context.length > 0) {
            root.put(BUSINESS_HEADER, context[0]);
        }
        StringWriter stringWriter = new StringWriter();

        try {
            root.put(BUSINESS_BODY, objectMapper.readValue(json, Map.class));
            templateToUse.process(root, stringWriter);
            if (!resourceTemplate.getTemplateValidations().isOkResponseJsonToXml(stringWriter.toString())) {
                Map<?, ?> response = xmlMapper.readValue(stringWriter.toString(), LinkedHashMap.class);
                ErrorConverse errorValidations = objectMapper.convertValue(response, ErrorConverse.class);
                throw new ConverseException(errorValidations);
            }
            return stringWriter.toString();

        } catch (IOException | TemplateException e) {
            throw new ConverseException(ErrorConverse.builder().error("Parsing Error jsonToXml").reason(e.getMessage()).build());
        }
    }

    @SneakyThrows
    @Override
    public <T> T xmlToObject(String xml, String templateCode, Class<T> target) {
        Map<String, Object> root = new HashMap<>();
        StringWriter stringWriter = new StringWriter();

        try {
            Map<?, ?> response = xmlMapper.readValue(xml, LinkedHashMap.class);

            TemplateTransactionFreemarker.ResourceTemplate resourceTemplate = getResourceTemplate(templateCode);
            Template templateToUse = getTemplate(response, resourceTemplate);

            root.put(BUSINESS_RESPONSE, response);
            templateToUse.process(root, stringWriter);
            if (!resourceTemplate.getTemplateValidations().isOkResponseXmlToObject(response)) {
                ErrorConverse errorValidations = objectMapper.readValue(stringWriter.toString(), ErrorConverse.class);
                throw new ConverseException(errorValidations);
            }
            return objectMapper.readValue(stringWriter.toString(), target);

        } catch (IOException | TemplateException e) {
            throw new ConverseException(ErrorConverse.builder().error("Parsing Error xmlToObject").reason(e.getMessage()).build());
        }
    }

    private TemplateTransactionFreemarker.ResourceTemplate getResourceTemplate(String templateCode) {
        return templateTransaction.get(templateCode);
    }

    private Template getTemplate(Map<?, ?> response, TemplateTransactionFreemarker.ResourceTemplate resourceTemplate) {
        return resourceTemplate.getTemplateValidations().isOkResponseXmlToObject(response) ? resourceTemplate.getTemplateXmlToJson() : resourceTemplate.getTemplateXmlToJsonError();
    }

    private Template getTemplate(String xml, TemplateTransactionFreemarker.ResourceTemplate resourceTemplate)  {
        return resourceTemplate.getTemplateValidations().isOkResponseJsonToXml(xml) ? resourceTemplate.getTemplateJsonToXml() : resourceTemplate.getTemplateJsonToXmlError();
    }

}