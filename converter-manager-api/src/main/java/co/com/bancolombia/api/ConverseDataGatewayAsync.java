package co.com.bancolombia.api;


import reactor.core.publisher.Mono;

public interface ConverseDataGatewayAsync {
    <T> Mono<T> xmlToObject(String xml,String templateCode, Class<T> target);

    Mono<String> jsonToXml(String json, String templateCode, Object... context) ;
}
