package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.MemberDTO;
import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskService taskService;

    @Test
    public void Task저장() throws Exception{
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        //when
        TaskDTO taskDTO = new TaskDTO(member, "백준", "DP");
        Long taskId = taskService.saveTask(taskDTO);
        Task task = taskRepository.findById(taskId);
        //then
        System.out.println("taskId = " + taskId + " task.getId() = " + task.getId());
        assertThat(taskId).isEqualTo(task.getId());
    }

    @Test
    public void 중복_이름_예외_Task저장(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);


        //when
        TaskDTO taskDTO = new TaskDTO(member, "백준", "DP");
        Long task1_id = taskService.saveTask(taskDTO);
        Task foundTask1 = taskRepository.findById(task1_id);
        System.out.println("foundTask1 = " + foundTask1.getMember().getName());

        //then
        assertThrows(TaskNameDuplicateException.class, () -> {
            TaskDTO taskDTO2 = new TaskDTO(member, "백준", "오잉");
            taskService.saveTask(taskDTO2);
        });
    }

    @Test
    public void 전체_Task조회() throws Exception{
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        TaskDTO taskDTO1 = new TaskDTO(member, "백준1", "DP");
        TaskDTO taskDTO2 = new TaskDTO(member, "백준2", "DP");
        TaskDTO taskDTO3 = new TaskDTO(member, "백준3", "DP");
        Long task1_id = taskService.saveTask(taskDTO1);
        Long task2_id = taskService.saveTask(taskDTO2);
        Long task3_id = taskService.saveTask(taskDTO3);

        //when
        List<TaskDTO> tasks = taskService.findAll(memberId);

        //then
        for(TaskDTO task : tasks) {
            System.out.println("tasks = " + task);
        }
        assertThat(tasks.size()).isEqualTo(3);
    }

    //find One
    @Test
    public void 이름_Task조회(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        TaskDTO taskDTO1 = new TaskDTO(member, "백준1", "DP");
        TaskDTO taskDTO2 = new TaskDTO(member, "백준2", "DP");
        TaskDTO taskDTO3 = new TaskDTO(member, "백준3", "DP");
        Long task1Id = taskService.saveTask(taskDTO1);
        Long task2Id = taskService.saveTask(taskDTO2);
        Long task3Id = taskService.saveTask(taskDTO3);
        Task task1 = taskRepository.findById(task1Id);

        //when
        TaskDTO findTasks = taskService.findOneByName(memberId, "task1");

        //then
        assertThat(findTasks.getId()).isEqualTo(task1.getId());

    }
    // update
    @Test
    public void 수정(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        TaskDTO taskDTO = new TaskDTO(member, "task1", "task1입니다.");
        Long task1Id = taskService.saveTask(taskDTO);
        Task task1 = taskRepository.findById(task1Id);

        Task updateData = Task.create(member, "task22", "task22입니다.task22입니다.");

        //when
        taskService.update(memberId, new TaskDTO(updateData));

        //then
        assertThat(task1.getName()).isEqualTo(updateData.getName());
        assertThat(task1.getDesc()).isNotEqualTo(updateData.getDesc());
    }

    //update - 중복 이름 예외
    @Test
    public void 중복_이름_예외_수정(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        TaskDTO taskDTO1 = new TaskDTO(member, "task1", "task1입니다.");
        TaskDTO taskDTO2 = new TaskDTO(member, "task2", "task2입니다.");
        Long task1Id = taskService.saveTask(taskDTO1);
        Task task1 = taskRepository.findById(task1Id);
        Long task2Id = taskService.saveTask(taskDTO2);

        //when

        Task updateData = Task.create(member, "task2", "task333입니다.task333입니다.");

        //then
        assertThrows(TaskNameDuplicateException.class, () -> {
            taskService.update(memberId, new TaskDTO(updateData));
        });
    }

    // delete
    @Test
    public void 삭제(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

        TaskDTO taskDTO = new TaskDTO(member, "task1", "task1입니다.");
        Long task1Id = taskService.saveTask(taskDTO);
        Task task1 = taskRepository.findById(task1Id);
        //when
        taskService.delete(task1);
        List<TaskDTO> tasks = taskService.findAll(memberId);

        //then
        assertThat(tasks.size()).isEqualTo(0);
    }
}