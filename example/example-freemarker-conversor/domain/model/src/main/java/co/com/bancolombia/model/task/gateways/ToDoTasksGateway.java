package co.com.bancolombia.model.task.gateways;

import co.com.bancolombia.model.context.Context;
import co.com.bancolombia.model.task.Task;
import reactor.core.publisher.Mono;

public interface ToDoTasksGateway {
    Mono<Task> getToDoTasks(String accountId, Context context);

}
