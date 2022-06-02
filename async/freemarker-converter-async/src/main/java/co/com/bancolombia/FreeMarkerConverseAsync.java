package co.com.bancolombia;

import co.com.bancolombia.api.ConverseDataGatewayAsync;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FreeMarkerConverseAsync implements ConverseDataGatewayAsync {

    private final FreeMarkerConverseSync freeMarkerConverseSync;

    @SneakyThrows
    @Override
    public <T> Mono<T> xmlToObject(String xml, String templateCode, Class<T> target) {
        return Mono.fromCallable(() -> freeMarkerConverseSync.xmlToObject(xml, templateCode, target));
    }

    @SneakyThrows
    @Override
    public Mono<String> jsonToXml(String json, String templateCode, Object... context) {
        return Mono.fromCallable(() -> freeMarkerConverseSync.jsonToXml(json, templateCode, context));
    }
}