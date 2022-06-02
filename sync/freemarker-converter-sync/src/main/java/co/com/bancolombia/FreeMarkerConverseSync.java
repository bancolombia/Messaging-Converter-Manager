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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class FreeMarkerConverseSync implements ConverseDataGateway {

    private static final String BUSINESS_HEADER = "businessHeader";
    private static final String DATE_FORMATTER = "dateFormatter";
    private static final String BUSINESS_BODY = "businessBody";
    private static final String BUSINESS_RESPONSE = "businessResponse";


    private final TemplateTransactionFreemarker templateTransaction;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    private Template getTemplate(Map<?, ?> response, TemplateTransactionFreemarker.ResourceTemplate resourceTemplate) throws IOException {
        return resourceTemplate.getTemplateValidations().isOkResponse(response) ? resourceTemplate.getTemplateOut() : resourceTemplate.getTemplateError();
    }

    private TemplateTransactionFreemarker.ResourceTemplate getResourceTemplate(String templateCode) {
        return templateTransaction.get(templateCode);
    }

    @SneakyThrows
    @Override
    public <T> T xmlToObject(String xml, String templateCode, Class<T> target) {
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, Object> root = new HashMap<>();
        StringWriter stringWriter = new StringWriter();

        try {
            Map<?, ?> response = xmlMapper.readValue(xml, LinkedHashMap.class);

            TemplateTransactionFreemarker.ResourceTemplate resourceTemplate = getResourceTemplate(templateCode);
            Template templateToUse = getTemplate(response, resourceTemplate);

            root.put(BUSINESS_RESPONSE, response);
            templateToUse.process(root, stringWriter);

            if (!resourceTemplate.getTemplateValidations().isOkResponse(response)) {
                ErrorConverse errorFreeMarker = objectMapper.readValue(stringWriter.toString(), ErrorConverse.class);
                throw new ConverseException(errorFreeMarker);
            }
            return objectMapper.readValue(stringWriter.toString(), target);
        } catch (IOException | TemplateException e) {
            throw new ConverseException(ErrorConverse.builder().error("Parsing Error xmlToObject").reason(e.getMessage()).build());
        }
    }

    @SneakyThrows
    @Override
    public String jsonToXml(String json, String templateCode, Object... context) {
        Template template = getResourceTemplate(templateCode).getTemplateIn();
        Map<String, Object> root = new HashMap<>();
        if (context.length > 0) {
            root.put(BUSINESS_HEADER, context[0]);
        }
        root.put(DATE_FORMATTER, formatter);
        StringWriter stringWriter = new StringWriter();

        try {
            root.put(BUSINESS_BODY, objectMapper.readValue(json, Map.class));
            template.process(root, stringWriter);
            return stringWriter.toString();

        } catch (IOException | TemplateException e) {
            throw new ConverseException(ErrorConverse.builder().error("Parsing Error jsonToXml").reason(e.getMessage()).build());
        }
    }
}