package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.RequestToDoTaskDTO;
import co.com.bancolombia.usecase.task.TaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.commons.ContextUtil.buildContext;

@Component
@RequiredArgsConstructor
public class Handler {
    private final TaskUseCase taskUseCase;


    public Mono<ServerResponse> retrieveToDoTasks(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RequestToDoTaskDTO.class)
                .flatMap(dto -> taskUseCase.getToDoTasks(dto.getNumber(),
                        buildContext(serverRequest.headers().asHttpHeaders().toSingleValueMap())))
                .flatMap(responseCostDTO -> ServerResponse.ok().bodyValue(responseCostDTO));
    }
}
