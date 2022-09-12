package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskDto {
    private Long id;
    private UserDto userDto;
    private String name;
    private String desc;

    public TaskDto(Task task){
        this.id = task.getId();
        this.userDto = new UserDto(task.getUser());
        this.name = task.getName();
        this.desc = task.getDesc();
    }

    public TaskDto(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    @Builder
    public TaskDto(Long id, UserDto userDto, String name, String desc){
        this.id = id;
        this.userDto = userDto;
        this.name = name;
        this.desc = desc;
    }
}
