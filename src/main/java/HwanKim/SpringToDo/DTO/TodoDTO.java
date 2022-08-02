package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Todo;
import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.domain.TodoTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TodoDTO {
    private Long id;
    private Member member;
    private List<TodoTask> todoTasks;
    private TodoTaskStatus status;
    private LocalDate createDate;

    public TodoDTO(Todo todo){
        this.id = todo.getId();
        this.member = todo.getMember();
        this.todoTasks = todo.getTodoTasks();
        this.status = todo.getStatus();
        this.createDate = todo.getCreatedDate();
    }
}
