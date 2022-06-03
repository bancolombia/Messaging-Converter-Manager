package co.com.bancolombia.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {


    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .clientConnector(getClientHttpConnector(
                       5000,
                        5000,
                        5000))
                .build();
    }

    private ClientHttpConnector getClientHttpConnector(int connectionTimeout, int readTimeout, int writeTimeout) {
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(writeTimeout, MILLISECONDS));
                }));
    }

}
