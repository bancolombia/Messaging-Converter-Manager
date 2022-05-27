package co.com.bancolombia.api;


import co.com.bancolombia.models.BusinessMessage;
import co.com.bancolombia.models.headers.Context;

public interface ConverseDataGateway {
    <T> T convertTOJSON(String xml, Context businessHeader, Class<T> target);

    String convertToXML(BusinessMessage businessMessage);
}
