package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Todo;
import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.domain.TodoTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TodoDto {
    private Long id;
    private List<TodoTaskDTO> todoTasks;
    private TodoTaskStatus status;
    private LocalDate createDate;

    public TodoDto(Todo todo){
        this.id = todo.getId();
        this.todoTasks = todo.getTodoTasks().stream()
                .map(TodoTaskDTO::new)
                .collect(Collectors.toList());
        this.status = todo.getStatus();
        this.createDate = todo.getCreatedDate();
    }
}
