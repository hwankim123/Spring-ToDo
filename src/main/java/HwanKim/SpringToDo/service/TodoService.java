package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.TodoDTO;
import HwanKim.SpringToDo.controller.Todo.TodoForm;
import HwanKim.SpringToDo.domain.*;
import HwanKim.SpringToDo.exception.TodoAlreadyExistException;
import HwanKim.SpringToDo.exception.TodoNotExistException;
import HwanKim.SpringToDo.exception.TodoTaskNameNullException;
import HwanKim.SpringToDo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    /**
     * 할일의 작업 list의 이름을 validate
     * 오늘의 할일이 이미 존재하는지 validate
     * validate 완료 후 오늘의 할일 생성
     */
    public Long saveTodo(Long memberId, String[] names, String[] descs){
        Member member = memberRepository.findById(memberId);

        validateName(names);

        List<TodoTask> todoTasks = new ArrayList<>();
        for(int i = 0; i < names.length; i++){
            todoTasks.add(TodoTask.createTodoTask(names[i], descs[i]));
        }
        Todo todo = Todo.create(member, todoTasks);
        todoRepository.save(todo);

        return todo.getId();
    }

    /**
     * 할일의 작업 개수가 0이거나 작업 이름이 없는 경우 예외처리
     */
    private void validateName(String[] names){
        if(names.length == 0){
            throw new TodoTaskNameNullException("작업 이름이 비어있는 항목이 존재합니다.");
        }
        for(String name : names){
            if(name.length() == 0){
                throw new TodoTaskNameNullException("작업 이름이 비어있는 항목이 존재합니다.");
            }
        }
    }

    /**
     * 오늘의 할일이 이미 존재하는 경우 예외처리
     */
    public void validateTodoAlreadyExist(Long memberId){
        List<Todo> todo = todoRepository.findTodayByMemberId(memberId);
        if(todo.size() != 0){
            throw new TodoAlreadyExistException("오늘의 할일이 이미 존재합니다.");
        }
    }

    /**
     * 오늘의 할일이 없는 경우 예외처리
     */
    public void validateTodoNotExist(Long memberId){
        List<Todo> todo = todoRepository.findTodayByMemberId(memberId);
        if(todo.size() == 0){
            throw new TodoNotExistException("오늘의 할일이 없습니다.");
        }
    }

    /**
     * todo id로 할일 조회
     */
    public TodoDTO findById(Long todoId) {
        return new TodoDTO(todoRepository.findById(todoId));
    }

    /**
     * todoSearch 객체에 담겨져 온 기간 조건으로 할일을 조회
     */
    public List<TodoDTO> searchTodo(TodoSearch todoSearch){
        validateTodoSearch(todoSearch);
        List<TodoDTO> collect = todoRepository.findAllByDate(todoSearch).stream()
                .map(TodoDTO::new)
                .collect(toList());
        for(TodoDTO todoDTO : collect){
            System.out.println("todoDTO.getId() = " + todoDTO.getId());
        }
        return collect;
    }

    /**
     * 오늘의 할일 조회
     */
    public TodoDTO findTodaysTodo(Long memberId) {
        return new TodoDTO(todoRepository.findTodayByMemberId(memberId).get(0));
    }


    /**
     * 할일의 작업 상태를 변경(완료 -> 미완료, 미완료 -> 완료)
     * 변경된 후의 할일을 return
     */
    public TodoDTO changeStatusOfTodoTask(Long todoId, Long todoTaskId, TodoTaskStatus status) {
        Todo todo = todoRepository.findById(todoId);
        todo.changeStatusOfTodoTask(todoTaskId, status);
        return new TodoDTO(todoRepository.findById(todoId));
    }

    /**
     * 오늘의 할일 수정
     */
    public void update(Long memberId, TodoForm todoForm){
        List<Todo> todaysTodo = todoRepository.findTodayByMemberId(memberId);
        List<TodoTask> todoTasks = todaysTodo.get(0).getTodoTasks();
        boolean[] isUpdated = new boolean[todoTasks.size()];
        Long[] idList = todoForm.getIds();
        String[] names = todoForm.getNames();
        String[] descs = todoForm.getDescs();


        validateName(names);

        for(int i = 0; i < idList.length; i++){
            if(idList[i] == -1){
                todoTasks.add(TodoTask.createTodoTask(names[i], descs[i]));
            } else{
                for(int j = 0; j < todoTasks.size(); j++){
                    TodoTask todoTask = todoTasks.get(j);
                    if(todoTask.getId().equals(idList[i])){
                        todoTask.update(names[i], descs[i]);
                        isUpdated[j] = true;
                    }
                }
            }
        }
        for(int i = 0; i < isUpdated.length; i++){
            if(!isUpdated[i]){
                TodoTask todoTask = todoTasks.get(i);
                todoTasks.remove(todoTask);
            }
        }

        todaysTodo.get(0).mappingTodoTasks();

    }

    /**
     * 오늘의 할일 삭제
     */
    public void delete(Long memberId) {
        List<Todo> todaysTodo = todoRepository.findTodayByMemberId(memberId);
        todoRepository.delete(todaysTodo.get(0));
    }

    /**
     * 회원 id 값이 null인 경우 예외처리
     * 시작 날짜 혹은 끝 날짜가 null인 경우 예외처리
     */
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
}
