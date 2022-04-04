package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.repository.MemberRepository;
import HwanKim.SpringToDo.repository.TaskRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
        Long memberId = memberService.signUp(member);

        Task task = new Task("백준", "DP");

        //when
        Long taskId = taskService.saveTask(memberId, task);

        //then
        System.out.println("taskId = " + taskId + " task.getId() = " + task.getId());
        assertThat(taskId).isEqualTo(task.getId());
    }

    @Test
    public void 중복_이름_예외_Task저장(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("백준", "DP");
        Long task1_id = taskService.saveTask(memberId, task1);

        //when
        Task task2 = new Task("백준", "오잉");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.saveTask(memberId, task2);
        });
    }

    @Test
    public void 전체_Task조회() throws Exception{
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("백준1", "DP");
        Long task1_id = taskService.saveTask(memberId, task1);
        Task task2 = new Task("백준2", "DP");
        Long task2_id = taskService.saveTask(memberId, task2);
        Task task3 = new Task("백준3", "DP");
        Long task3_id = taskService.saveTask(memberId, task3);

        //when
        List<Task> tasks = taskService.findAll(memberId);

        //then
        for(Task task : tasks) {
            System.out.println("tasks = " + task);
        }
        assertThat(tasks.size()).isEqualTo(3);
    }

    //find One
    @Test
    public void 이름_Task조회(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("task1", "task1입니다.");
        Long task1Id = taskService.saveTask(memberId, task1);
        Task task2 = new Task("task2", "task2입니다.");
        Long task2Id = taskService.saveTask(memberId, task2);
        Task task3 = new Task("task3", "task3입니다.");
        Long task3Id = taskService.saveTask(memberId, task3);

        //when
        List<Task> findTasks = taskService.findOne(memberId, "task1");

        //then
        assertThat(findTasks.size()).isEqualTo(1);
        assertThat(findTasks.get(0).getId()).isEqualTo(task1.getId());

    }
    // update
    @Test
    public void 수정(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("task1", "task1입니다.");
        Long task1Id = taskService.saveTask(memberId, task1);

        Task updateData = new Task("task22", "task22입니다.task22입니다.");

        //when
        taskService.update(memberId, task1, updateData.getName(), null);

        //then
        assertThat(task1.getName()).isEqualTo(updateData.getName());
        assertThat(task1.getDesc()).isNotEqualTo(updateData.getDesc());
    }

    //update - 중복 이름 예외
    @Test
    public void 중복_이름_예외_수정(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("task1", "task1입니다.");
        Long task1Id = taskService.saveTask(memberId, task1);
        Task task2 = new Task("task2", "task2입니다.");
        Long task2Id = taskService.saveTask(memberId, task2);

        //when

        Task updateData = new Task("task2", "task333입니다.task333입니다.");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.update(memberId, task1, updateData.getName(), null);
        });
    }

    // delete
    @Test
    public void 삭제(){
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("task1", "task1입니다.");
        Long task1Id = taskService.saveTask(memberId, task1);

        //when
        taskService.delete(task1);
        List<Task> tasks = taskService.findAll(memberId);

        //then
        assertThat(tasks.size()).isEqualTo(0);
    }
}