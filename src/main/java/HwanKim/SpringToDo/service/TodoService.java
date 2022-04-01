package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.*;
import HwanKim.SpringToDo.repository.*;
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
    private final TodoTaskRepository todoTaskRepository;

    public Long saveTodo(Long memberId, List<Long> taskIdList){
        Member member = memberRepository.findById(memberId);
        List<TodoTask> todoTasks = new ArrayList<>();
        for(int idx = 0; idx < taskIdList.size(); idx++){
            Task task = taskRepository.findById(taskIdList.get(idx));
            todoTasks.add(TodoTask.createTodoTask(task, TodoTaskStatus.READY));
        }

        Todo todo = Todo.create(member, todoTasks);
        todoRepository.save(todo);

        return todo.getId();
    }

    public List<Todo> searchTodo(TodoSearch todoSearch){
        return todoRepository.findAllByDate(todoSearch);
    }

    /**
     * Update Todo
     */

    /**
     * Delete Todo
     */
}
