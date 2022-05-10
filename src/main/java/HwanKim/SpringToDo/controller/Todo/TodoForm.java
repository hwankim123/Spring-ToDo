package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.controller.Task.TaskForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class TodoForm {
    private List<TaskDTO> tasks = new ArrayList<>();

    public void setTasks(List tasks){
        this.tasks = tasks;
    }
    public void addTask(TaskDTO task){this.tasks.add(task);}

}
