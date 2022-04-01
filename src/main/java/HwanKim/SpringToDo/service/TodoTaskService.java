package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.repository.TodoTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;

    public void startTodoTask(Long todoTaskId){
        TodoTask todoTask = todoTaskRepository.findById(todoTaskId);
        todoTask.start();
    }

    public void pauseTodoTask(Long todoTaskId){
        TodoTask todoTask = todoTaskRepository.findById(todoTaskId);
        todoTask.pause();
    }

    public void finishTodoTask(Long todoTaskId){
        TodoTask todoTask = todoTaskRepository.findById(todoTaskId);
        todoTask.finish();
    }
}
