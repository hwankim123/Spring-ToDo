package HwanKim.SpringToDo.controller.Todo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter @Setter
public class TodoForm {
    private Long[] ids;
    @NotEmpty
    private String[] names;
    private String[] descs;
}
