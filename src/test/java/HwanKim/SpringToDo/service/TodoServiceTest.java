package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.domain.Todo;
import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.repository.TodoRepository;
import HwanKim.SpringToDo.repository.TodoSearch;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    private TodoService todoService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void todo_저장_조회() throws Exception{
        //given
        Long memberId1 = setMemberId("김환", "hwankim123", "aefewfws");
        Long memberId2 = setMemberId("김환123", "hwankim123123", "aefewfws");
        Long memberId3 = setMemberId("김환324", "hwankim342123", "aefewfws");
        List<Long> taskIdList1 = setTaskId123(memberId1);
        List<Long> taskIdList2 = setTaskId12(memberId2);
        List<Long> taskIdList3 = setTaskId23(memberId3);
        //when
        todoService.saveTodo(memberId1, taskIdList1, setDescList("백준 1문제", "Spring 프로젝트 개발", "캡스톤"));
        todoService.saveTodo(memberId2, taskIdList2, setDescList("백준 1문제", "Spring 프로젝트 개발"));
        todoService.saveTodo(memberId3, taskIdList3, setDescList("Spring 프로젝트 개발", "캡스톤"));

        //then
        List<Todo> todoList1 = todoRepository.findAllByMemberId(memberId1);
        for(Todo todo : todoList1){
            List<TodoTask> todoTasks = todo.getTodoTasks();
            int i = 0;
            for(TodoTask todoTask : todoTasks){
                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList1.get(i));
                i++;
            }
        }
        List<Todo> todoList2 = todoRepository.findAllByMemberId(memberId2);
        for(Todo todo : todoList2){
            List<TodoTask> todoTasks = todo.getTodoTasks();
            int i = 0;
            for(TodoTask todoTask : todoTasks){
                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList2.get(i));
                i++;
            }
        }
        List<Todo> todoList3 = todoRepository.findAllByMemberId(memberId3);
        for(Todo todo : todoList3){
            List<TodoTask> todoTasks = todo.getTodoTasks();
            int i = 0;
            for(TodoTask todoTask : todoTasks){
                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList3.get(i));
                i++;
            }
        }
    }

    private List<String> setDescList(String ...descs) {
        List<String> descList = new ArrayList<>();
        for(String desc : descs){
            descList.add(desc);
        }
        return descList;
    }

    @Test
    public void todo_조회_예외(){
        //given
        Long memberId = setMemberId("김환", "hwankim123", "aefewfws");
        List<Long> taskIdList = setTaskId123(memberId);

        //when
        Long todoId = todoService.saveTodo(memberId, taskIdList, setDescList("백준 1문제", "Spring 프로젝트 개발", "캡스톤"));
        TodoSearch todoSearch = new TodoSearch();
        
        // 이 셋중에 테스트하고싶은걸 주석처리
        todoSearch.setMemberId(memberId);
        //todoSearch.setStartDate(LocalDateTime.of(2022, 4, 3, 12, 32,22,3333));
        todoSearch.setEndDate(LocalDateTime.of(2022, 11, 12, 12, 32,22,3333));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            todoService.searchTodo(todoSearch);
        });
    }

    private Long setMemberId(String name, String username, String password){
        Member member = new Member(name, username, password);
        return memberService.signUp(member);
    }

    private List<Long> setTaskId123(Long memberId){
        Task task1 = new Task("task1", "task1입니다");
        Task task2 = new Task("task2", "task2입니다");
        Task task3 = new Task("task3", "task3입니다");
        Long task1Id = taskService.saveTask(memberId, task1);
        Long task2Id = taskService.saveTask(memberId, task2);
        Long task3Id = taskService.saveTask(memberId, task3);

        List<Long> taskIdList = new ArrayList<>();
        taskIdList.add(task1Id);
        taskIdList.add(task2Id);
        taskIdList.add(task3Id);
        return taskIdList;
    }

    private List<Long> setTaskId23(Long memberId) {
        Task task2 = new Task("task2", "task2입니다");
        Task task3 = new Task("task3", "task3입니다");
        Long task2Id = taskService.saveTask(memberId, task2);
        Long task3Id = taskService.saveTask(memberId, task3);

        List<Long> taskIdList = new ArrayList<>();
        taskIdList.add(task2Id);
        taskIdList.add(task3Id);
        return taskIdList;
    }

    private List<Long> setTaskId12(Long memberId) {
        Task task1 = new Task("task1", "task1입니다");
        Task task2 = new Task("task2", "task2입니다");
        Long task1Id = taskService.saveTask(memberId, task1);
        Long task2Id = taskService.saveTask(memberId, task2);

        List<Long> taskIdList = new ArrayList<>();
        taskIdList.add(task1Id);
        taskIdList.add(task2Id);
        return taskIdList;
    }
}