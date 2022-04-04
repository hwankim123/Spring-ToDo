package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.domain.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    private Member member;
    List<Long> taskIdList;
    Todo todo;

    /**
     * startTodoTest test
     */

    /**
     * pauseTodoTest test
     */

    /**
     * finishTodoTest test
     */

    private void prepareTest(){
        member = new Member("김환", "hwankim123", "cjsak123");
        Long memberId = memberService.signUp(member);

        Task task1 = new Task("백준", "백준 1문제");
        Task task2 = new Task("클컴 과제", "클컴 과제");
        Task task3 = new Task("캡디 면담", "면담 하고 면담확인서 제출");
        taskIdList.add(taskService.saveTask(memberId, task1));
        taskIdList.add(taskService.saveTask(memberId, task2));
        taskIdList.add(taskService.saveTask(memberId, task3));

        // todo Setting 해야함
    }
}
