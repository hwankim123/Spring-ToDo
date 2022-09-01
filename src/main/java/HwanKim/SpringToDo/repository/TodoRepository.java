package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoRepository {

    private final EntityManager em;

    public void save(Todo todo){
        em.persist(todo);
    }

    public Todo findById(Long todoId){
        return em.find(Todo.class, todoId);
    }

    public List<Todo> findAllByMemberId(Long memberId){
        return em.createQuery("select t From Todo t where t.member.id = :memberId")
                .setParameter("memberId", memberId).getResultList();
    }

    public List<Todo> findTodayByMemberId(Long memberId) throws RuntimeException{
        String jpql = "select t From Todo t where t.member.id = :memberId and t.createdDate = :today";
        return em.createQuery(jpql, Todo.class)
                    .setParameter("memberId", memberId)
                    .setParameter("today", LocalDate.now())
                    .getResultList();
    }

    public List<Todo> findTodayByUserId(Long userId) throws RuntimeException{
        String jpql = "select t From Todo t where t.user.id = :userId and t.createdDate = :today";
        return em.createQuery(jpql, Todo.class)
                .setParameter("userId", userId)
                .setParameter("today", LocalDate.now())
                .getResultList();
    }

    public List<Todo> findAllByDate(TodoSearch todoSearch){
        String jpql = "select t From Todo t where t.member.id = :memberId and t.createdDate between :startDate and :endDate";

        return em.createQuery(jpql, Todo.class)
                .setParameter("memberId", todoSearch.getMemberId())
                .setParameter("startDate", todoSearch.getStartDate())
                .setParameter("endDate", todoSearch.getEndDate())
                .getResultList();
    }

    public void delete(Todo todo) {
        em.remove(todo);
    }
}
