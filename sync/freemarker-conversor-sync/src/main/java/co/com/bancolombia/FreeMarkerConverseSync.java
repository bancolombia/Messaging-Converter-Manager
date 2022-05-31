package co.com.bancolombia;

import co.com.bancolombia.api.ConverseDataGateway;
import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.models.ErrorConverse;
import co.com.bancolombia.models.TemplateTransactionFreemarker;
import co.com.bancolombia.models.headers.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
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
    private static final String HEADERS = "HEADERRS";
    private static final String STATUS = "STATUS";
    private static final String CODE = "CODE";
    private static final String OK_CODE = "000";

    private final TemplateTransactionFreemarker templateTransaction;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");



    private Template getTemplate(Map<?, ?> status, String transactionCode) throws IOException {
        return isOkTransaction(status) ? getResourceTemplate
                (transactionCode).getTemplateOut() : getResourceTemplate
                (transactionCode).getTemplateError();
    }

    private boolean isOkTransaction(Map<?, ?> status) {
        return status.get(CODE).equals(OK_CODE);
    }

    private TemplateTransactionFreemarker.ResourceTemplate getResourceTemplate(String transactionCode) {
        return templateTransaction.get(transactionCode);
    }

    @Override
    public <T> T xmlToObject(String xml, Context context, Class<T> target) throws ConverseException {
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, Object> root = new HashMap<>();
        StringWriter stringWriter = new StringWriter();

        try {
            Map<?,?> value = xmlMapper.readValue(xml, LinkedHashMap.class);
            Map<?,?> status = (Map<?,?>)((Map<?,?>) value.get(HEADERS)).get(STATUS);

            Template template = getTemplate(status, context.getTransactionCode());
            root.put(BUSINESS_RESPONSE, value);
            template.process(root, stringWriter);


            if(!isOkTransaction(status)) {
                ErrorConverse errorFreeMarker =objectMapper.readValue(stringWriter.toString(), ErrorConverse.class);
                    throw new  ConverseException( errorFreeMarker);
            }
            return objectMapper.readValue(stringWriter.toString(), target);



        } catch (IOException | TemplateException e) {
            throw new ConverseException(e, "Parsing Error");
        }
    }

    @Override
    public String jsonToXml(String json, Context context) throws ConverseException {
        Template template = getResourceTemplate(context.getTransactionCode()).getTemplateIn();
        Map<String, Object> root = new HashMap<>();
        root.put(BUSINESS_HEADER, context);
        root.put(DATE_FORMATTER, formatter);
        StringWriter stringWriter = new StringWriter();

        try {
            root.put(BUSINESS_BODY, objectMapper.readValue(json, Map.class));
            template.process(root, stringWriter);
            return stringWriter.toString();

        } catch (IOException | TemplateException e) {
            throw new ConverseException(e, "Parsing Error");
        }
    }
}