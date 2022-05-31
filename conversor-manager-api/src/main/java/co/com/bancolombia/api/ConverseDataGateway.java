package co.com.bancolombia.api;


import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.models.headers.Context;

public interface ConverseDataGateway {
    <T> T xmlToObject(String xml, Context context, Class<T> target) throws ConverseException;

    String jsonToXml(String json, Context context) throws ConverseException;
}
