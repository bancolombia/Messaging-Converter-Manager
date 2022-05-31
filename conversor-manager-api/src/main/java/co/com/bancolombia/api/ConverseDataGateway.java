package co.com.bancolombia.api;


import co.com.bancolombia.exceptions.ConverseException;

public interface ConverseDataGateway {
    <T> T xmlToObject(String xml, String transactionCode, Class<T> target) throws ConverseException;

    String jsonToXml(String json, String transactionCode, Object context) throws ConverseException;
}
