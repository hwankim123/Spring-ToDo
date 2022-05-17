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

    public Long saveTodo(Long memberId, String[] names, String[] descs){
        Member member = memberRepository.findById(memberId);

        List<TodoTask> todoTasks = new ArrayList<>();
        for(int i = 0; i < names.length; i++){
            todoTasks.add(TodoTask.createTodoTask(names[i], descs[i], TodoTaskStatus.READY));
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

    public Todo findTodaysTodo(Long memberId) {
        return todoRepository.findTodayByMemberId(memberId);
    }

    /**
     * Update Todo
     */

    /**
     * Delete Todo
     */
}
