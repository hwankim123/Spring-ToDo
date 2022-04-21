package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.MemberDTO;
import HwanKim.SpringToDo.domain.*;
import HwanKim.SpringToDo.repository.TaskRepository;
import HwanKim.SpringToDo.repository.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class TodoTaskServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TodoService todoService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TodoTaskService todoTaskService;
    @Autowired
    private TodoRepository todoRepository;

    private Member member;
    private List<Long> taskIdList = new ArrayList<>();
    private Long todoId;
    private TodoTask todoTask;

    /**
     * startTodoTest test
     */
    @Test
    public void todoStart() throws Exception{
        prepareTest();
        Todo todo = todoRepository.findById(todoId);
        todoTask = todo.getTodoTasks().get(0);
        todoTaskService.startTodoTask(todoTask.getId());

        System.out.println("todoTask.getStartTime()" + todoTask.getStartTime());
        System.out.println("todoTask.getRestartTime()" + todoTask.getRestartTime());
        Assertions.assertThat(todoTask.getStatus()).isEqualTo(TodoTaskStatus.RUNNING);
    }

    /**
     * pauseTodoTest test
     */
    @Test
    public void todoPause() throws Exception{
        prepareTest();
        Todo todo = todoRepository.findById(todoId);
        todoTask = todo.getTodoTasks().get(0);
        todoTaskService.startTodoTask(todoTask.getId());
        Thread.sleep(3000);
        todoTaskService.pauseTodoTask(todoTask.getId());

        System.out.println("todoTask.getTaskDuration().getSeconds() : " + todoTask.getTaskDuration().getSeconds());
        Assertions.assertThat(todoTask.getStatus()).isEqualTo(TodoTaskStatus.PAUSE);
    }

    /**
     * finishTodoTest test
     */
    @Test
    public void todoFinish() throws Exception{
        prepareTest();
        Todo todo = todoRepository.findById(todoId);
        todoTask = todo.getTodoTasks().get(0);
        todoTaskService.startTodoTask(todoTask.getId());
        Thread.sleep(3000);
        todoTaskService.pauseTodoTask(todoTask.getId());
        Thread.sleep(1000);
        todoTaskService.startTodoTask(todoTask.getId());
        Thread.sleep(4000);
        todoTaskService.pauseTodoTask(todoTask.getId());
        Thread.sleep(1000);
        todoTaskService.startTodoTask(todoTask.getId());
        Thread.sleep(4000);
        todoTaskService.finishTodoTask(todoTask.getId());

        System.out.println("todoTask.getTaskDuration().getSeconds() : " + todoTask.getTaskDuration().getSeconds());
        System.out.println("todo.startTime() / todo.finishTime() : " + todo.getStartTime() + " / " + todo.getFinishTime());
        Assertions.assertThat(todoTask.getStatus()).isEqualTo(TodoTaskStatus.FINISH);
    }

    private void prepareTest(){
        member = new Member("김환", "hwankim123", "cjsak123");
        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
        Long memberId = memberService.signUp(memberDTO);

//        Task task2 = new Task("클컴 과제", "클컴 과제");
//        Task task3 = new Task("캡디 면담", "면담 하고 면담확인서 제출");
        List<String> descList = new ArrayList<>();
        Long task1Id = taskService.saveTask(memberId, "백준", "백준 1문제");
        Task task1 = taskRepository.findById(task1Id);
        taskIdList.add(task1Id);
        descList.add(task1.getDesc());
//        taskIdList.add(taskService.saveTask(memberId, task2));
//        descList.add(task2.getDesc());
//        taskIdList.add(taskService.saveTask(memberId, task3));
//        descList.add(task3.getDesc());

        todoId = todoService.saveTodo(memberId, taskIdList, descList);
    }
}
