package HwanKim.SpringToDo.controller.Todo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class TodoForm {
    private Long[] ids;
    @NotNull(message="작업을 최소 한개 이상 등록해주세요.")
    private String[] names;
    private String[] descs;
}
