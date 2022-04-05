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

    public Long saveTodo(Long memberId, List<Long> taskIdList, List<String> descList){
        Member member = memberRepository.findById(memberId);
        List<TodoTask> todoTasks = new ArrayList<>();
        for(int idx = 0; idx < taskIdList.size(); idx++){
            Task task = taskRepository.findById(taskIdList.get(idx));
            todoTasks.add(TodoTask.createTodoTask(task, TodoTaskStatus.READY, descList.get(idx)));
        }

        Todo todo = Todo.create(member, todoTasks);
        todoRepository.save(todo);

        return todo.getId();
    }

    public List<Todo> searchTodo(TodoSearch todoSearch){
        validateTodoSearch(todoSearch);
        return todoRepository.findAllByDate(todoSearch);
    }

    private void validateTodoSearch(TodoSearch todoSearch) {
        if (todoSearch.getMemberId() == null) {
            throw new IllegalArgumentException("회원 id 값은 필수입니다.");
        }
        if(todoSearch.getStartDate() == null && todoSearch.getEndDate() != null){
            throw new IllegalArgumentException("끝 날짜는 존재하지만 시작 날짜가 존재하지 않습니다.");
        }
        else if(todoSearch.getStartDate() != null && todoSearch.getEndDate() == null){
            throw new IllegalArgumentException("시작 날짜는 존재하지만 끝 날짜가 존재하지 않습니다.");
        }
    }

    /**
     * Update Todo
     */

    /**
     * Delete Todo
     */
}
