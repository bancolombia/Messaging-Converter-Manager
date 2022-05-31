package co.com.bancolombia.api;


import co.com.bancolombia.exceptions.ConverseException;
import reactor.core.publisher.Mono;

public interface ConverseDataGatewayAsync {
    <T> Mono<T> xmlToObject(String xml,String transactionCode, Class<T> target) throws ConverseException;

    Mono<String> jsonToXml(String json, String transactionCode, Object context) throws ConverseException;
}
