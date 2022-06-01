package co.com.bancolombia.consumer;

import co.com.bancolombia.FreeMarkerConverseAsync;
import co.com.bancolombia.exceptions.ConverseException;
import co.com.bancolombia.model.context.Context;
import co.com.bancolombia.model.exception.TechnicalException;
import co.com.bancolombia.model.exception.TechnicalExceptionEnum;
import co.com.bancolombia.model.task.Task;
import co.com.bancolombia.model.task.gateways.ToDoTasksGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements ToDoTasksGateway {

    private final WebClient webClient;

    private final FreeMarkerConverseAsync freeMarkerConverse;

    @Override
    public Mono<Task> getToDoTasks(String accountId, Context context) {

        String body = "{" + "\"account\":\"" + accountId + "\"}";
        return freeMarkerConverse.jsonToXml(body, context.getTemplateCode(), context).flatMap(msg -> webClient.get()
                        .uri("http://localhost:3000/external-service/tasks/todo")
                        .header("msg", msg)
                        .exchangeToMono(clientResponse -> {
                            if (clientResponse.statusCode().is2xxSuccessful()) {
                                return clientResponse.bodyToMono(String.class);
                            }
                            return clientResponse.createException().flatMap(Mono::error);
                        }))
                .flatMap(objectResponse -> freeMarkerConverse.xmlToObject(objectResponse, context.getTemplateCode(), Task.class))
                .onErrorMap(WebClientResponseException.InternalServerError.class,
                        error -> new TechnicalException(error, TechnicalExceptionEnum.UNKNOWN_ERROR))
                .onErrorMap(ConverseException.class,
                        error -> new TechnicalException(error, TechnicalExceptionEnum.getByError(error.getErrorCode())));
    }


}