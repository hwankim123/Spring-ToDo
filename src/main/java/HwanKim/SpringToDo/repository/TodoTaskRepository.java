package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.TodoTask;
import HwanKim.SpringToDo.domain.TodoTaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoTaskRepository {

    private final EntityManager em;

    public TodoTask findById(Long todoTaskId){
        return em.find(TodoTask.class, todoTaskId);
    }

    public List<TodoTask> findAllByStatus(Long todoId, TodoTaskStatus status){
        return em.createQuery("select tt From TodoTask tt  where tt.todo.id = :todoId and tt.status = :status")
                .setParameter("todoId", todoId)
                .setParameter("status", status)
                .getResultList();
    }
}
