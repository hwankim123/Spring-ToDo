package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.domain.TodoTaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodoTaskStatusForm {
    Long todoId;
    Long todoTaskId;
    TodoTaskStatus status;
}
