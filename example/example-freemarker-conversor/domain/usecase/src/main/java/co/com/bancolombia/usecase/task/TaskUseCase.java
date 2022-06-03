package co.com.bancolombia.usecase.task;

import co.com.bancolombia.model.context.Context;
import co.com.bancolombia.model.task.Task;
import co.com.bancolombia.model.task.gateways.ToDoTasksGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TaskUseCase {
    private final ToDoTasksGateway tasksGateway;

    public Mono<Task> getToDoTasks(String accountId, Context context) {
        return tasksGateway.getToDoTasks(accountId, context);
    }
}
