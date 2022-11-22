package co.com.bancolombia.api;
import java.util.Map;

public interface TemplateValidations {
    boolean isOkResponseXmlToObject(Map<?, ?> object);

    boolean isOkResponseJsonToXml(String json);

}
