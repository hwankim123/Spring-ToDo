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
        assertThat(tasks.size()).isEqualTo(3);
    }

    //find One

    // update

    // delete
}