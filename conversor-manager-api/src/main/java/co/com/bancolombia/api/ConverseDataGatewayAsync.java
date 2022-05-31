package co.com.bancolombia.api;


import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.models.headers.Context;
import reactor.core.publisher.Mono;

public interface ConverseDataGatewayAsync {
    <T> Mono<T> xmlToObject(String xml, Context context, Class<T> target) throws ConverseException;

    Mono<String> jsonToXml(String json, Context context) throws ConverseException;
}
