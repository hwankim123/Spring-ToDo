package HwanKim.SpringToDo.controller.Task;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class TaskForm {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;
    private String desc;
}
