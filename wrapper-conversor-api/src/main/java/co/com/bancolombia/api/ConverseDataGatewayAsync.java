package co.com.bancolombia.api;


import co.com.bancolombia.models.BusinessMessage;
import co.com.bancolombia.models.headers.Context;
import reactor.core.publisher.Mono;

public interface ConverseDataGatewayAsync {
    <T> Mono<T> convertTOJSON(String xml, Context businessHeader, Class<T> target);

    Mono<String> convertToXML(BusinessMessage businessMessage);
}
