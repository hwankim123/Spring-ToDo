package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.domain.Todo;
import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.repository.MemberRepository;
import HwanKim.SpringToDo.repository.TaskRepository;
import HwanKim.SpringToDo.repository.TodoRepository;
import HwanKim.SpringToDo.repository.TodoSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    public Long saveTodo(Long memberId, List<Long> taskIdList, List<Duration> durationList){
        Member member = memberRepository.findById(memberId);
        List<TodoTask> todoTasks = new ArrayList<>();
        for(int idx = 0; idx < taskIdList.size(); idx++){
            Task task = taskRepository.findById(taskIdList.get(idx));
            todoTasks.add(TodoTask.createTodoTask(task, durationList.get(idx)));
        }

        Todo todo = Todo.create(member, todoTasks);
        todoRepository.save(todo);

        return todo.getId();
    }

    public List<Todo> searchTodo(TodoSearch todoSearch){
        return todoRepository.findAllByDate(todoSearch);
    }

    public void startTodo(Long todoId){
        Todo todo = todoRepository.findById(todoId);

        todo.start();
    }

    /**
     * Pause Todo
     * TodoStatus를 Pause로 변경. 현재 진행중이던 TaskStatus도 Pause로 변경.(현재 진행중인 Task를 저장하는 필드 추가해야 할듯)
     * Pause 기능까지 생각했을 때 시간 계산의 로직을 어떻게 짤지 전체적으로 생각해봐야함
     */
    public void pauseTodo(Long todoId){
        Todo todo = todoRepository.findById(todoId);

        todo.pause();
    }


    /**
     * Update Todo
     */

    /**
     * Delete Todo
     */
}
