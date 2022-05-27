package co.com.bancolombia;

import co.com.bancolombia.api.ConverseDataGateway;
import co.com.bancolombia.freemarkerconversor.model.ErrorFreeMarker;
import co.com.bancolombia.model.exception.TechnicalException;
import co.com.bancolombia.model.exception.TechnicalExceptionEnum;
import co.com.bancolombia.models.BusinessMessage;
import co.com.bancolombia.models.TemplateTransaction;
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
public class FreeMarkerConverse implements ConverseDataGateway {

    private static final String BUSINESS_HEADER = "businessHeader";
    private static final String DATE_FORMATTER = "dateFormatter";
    private static final String BUSINESS_BODY = "businessBody";
    private static final String BUSINESS_RESPONSE = "businessResponse";
    private static final String HEADERS = "HEADERRS";
    private static final String STATUS = "STATUS";
    private static final String CODE = "CODE";
    private static final String OK_CODE = "000";

    private final TemplateTransaction templateTransaction;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    @Override
    public <T> T convertTOJSON(String xml, Context businessHeader, Class<T> target) {

        XmlMapper xmlMapper = new XmlMapper();
        Map<String, Object> root = new HashMap<>();
        StringWriter stringWriter = new StringWriter();

        try {
            Map<?,?> value = xmlMapper.readValue(xml, LinkedHashMap.class);
            Map<?,?> status = (Map<?,?>)((Map<?,?>) value.get(HEADERS)).get(STATUS);

            Template template = getTemplate(status, businessHeader.getTransactionCode());
            root.put(BUSINESS_RESPONSE, value);
            template.process(root, stringWriter);


             if(!isOkTransaction(status)) {
                 ErrorFreeMarker errorFreeMarker =objectMapper.readValue(stringWriter.toString(), ErrorFreeMarker.class);
                 if(errorFreeMarker.getCode().equals("406")){
                     throw new  TechnicalException(new RuntimeException(), TechnicalExceptionEnum.PARSING_ERROR);

                 }
                 else{
                     throw new  TechnicalException(new RuntimeException(), TechnicalExceptionEnum.UNKNOWN_ERROR);

                 }

             }
            return objectMapper.readValue(stringWriter.toString(), target);



        } catch (IOException | TemplateException e) {
            throw new  TechnicalException(new RuntimeException(), TechnicalExceptionEnum.PARSING_ERROR);
        }

    }

    @Override
    public String convertToXML(BusinessMessage businessMessage) {

        Template template = getResourceTemplate
                (businessMessage.getContext().getTransactionCode()).getTemplateIn();
        Map<String, Object> root = new HashMap<>();
        root.put(BUSINESS_HEADER, businessMessage.getContext());
        root.put(DATE_FORMATTER, formatter);
        StringWriter stringWriter = new StringWriter();

        try {
            root.put(BUSINESS_BODY, objectMapper.readValue(businessMessage.getBody(), Map.class));
            template.process(root, stringWriter);
            return stringWriter.toString();

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Template getTemplate(Map<?, ?> status, String transactionCode) throws IOException {
        return isOkTransaction(status) ? getResourceTemplate
                (transactionCode).getTemplateOut() : getResourceTemplate
                (transactionCode).getTemplateError();
    }

    private boolean isOkTransaction(Map<?, ?> status) {
        return status.get(CODE).equals(OK_CODE);
    }

    private TemplateTransaction.ResourceTemplate getResourceTemplate(String transactionCode) {
        return templateTransaction.get(transactionCode);
    }
}