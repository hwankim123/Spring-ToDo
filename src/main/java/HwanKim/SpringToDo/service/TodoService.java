package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.*;
import HwanKim.SpringToDo.exception.TodoAlreadyExistException;
import HwanKim.SpringToDo.exception.TodoTaskNameNullException;
import HwanKim.SpringToDo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    public Long saveTodo(Long memberId, String[] names, String[] descs){
        Member member = memberRepository.findById(memberId);

        List<TodoTask> todoTasks = new ArrayList<>();
        for(int i = 0; i < names.length; i++){
            todoTasks.add(TodoTask.createTodoTask(names[i], descs[i]));
        }

        validateName(names);
        validateTodo(memberId);
        Todo todo = Todo.create(member, todoTasks);
        todoRepository.save(todo);

        return todo.getId();
    }

    private void validateName(String[] names){
        if(names.length == 0){
            throw new TodoTaskNameNullException("작업 이름 값은 필수입니다.");
        }
        for(String name : names){
            if(name.length() == 0){
                throw new TodoTaskNameNullException("작업 이름 값은 필수입니다.");
            }
        }
    }

    private void validateTodo(Long memberId){
        List<Todo> todo = todoRepository.findTodayByMemberId(memberId);
        if(todo.size() != 0){
            throw new TodoAlreadyExistException("오늘의 할일이 이미 존재합니다.");
        }
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
        return todoRepository.findTodayByMemberId(memberId).get(0);
    }

    /**
     * Update Todo
     */

    /**
     * Delete Todo
     */
    public void delete(Long memberId) {
        List<Todo> todaysTodo = todoRepository.findTodayByMemberId(memberId);
        todoRepository.delete(todaysTodo.get(0));
    }

    public Todo changeStatusOfTodoTask(Long todoId, Long todoTaskId, TodoTaskStatus status) {
        Todo todo = todoRepository.findById(todoId);
        todo.changeStatusOfTodoTask(todoTaskId, status);
        return todoRepository.findById(todoId);
    }

    public Todo findById(Long todoId) {
        return todoRepository.findById(todoId);
    }
}
