package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskDTO {
    private Long id;
    private Member member;
    private String name;
    private String desc;

    public TaskDTO(Task task){
        this.id = task.getId();
        this.member = task.getMember();
        this.name = task.getName();
        this.desc = task.getDesc();
    }
}
