package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.domain.TodoTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TodoTaskDTO {
    private Long id;

    private String name;
    private String desc;
    private TodoTaskStatus status;

    public TodoTaskDTO(TodoTask todoTask){
        this.id = todoTask.getId();
        this.name = todoTask.getName();
        this.desc = todoTask.getDesc();
        this.status = todoTask.getStatus();
    }
}
