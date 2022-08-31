package HwanKim.SpringToDo.DTO;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskDto {
    private Long id;
    private Member member;
    private UserDto userDto;
    private String name;
    private String desc;

    public TaskDto(Task task){
        this.id = task.getId();
        this.member = task.getMember();
        this.name = task.getName();
        this.desc = task.getDesc();
    }

    public TaskDto(Long id, String name, String desc){
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public TaskDto(Member member, String name, String desc){
        this.member = member;
        this.name = name;
        this.desc = desc;
    }

    public TaskDto(Long id, Member member, String name, String desc){
        this.id = id;
        this.member = member;
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
