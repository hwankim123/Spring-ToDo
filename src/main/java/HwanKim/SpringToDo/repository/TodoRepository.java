package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    public List<Todo> findAllByMemberId(Long memberId ){
        return em.createQuery("select t From Todo t where t.member.id = :memberId")
                .setParameter("memberId", memberId).getResultList();
    }

    public List<Todo> findAllByDate(TodoSearch todoSearch){
        String jpql = "select t From Todo t join t.todoTasks tt";

        //회원 id 검색
        if (todoSearch.getMemberId() == null) {
            throw new IllegalArgumentException("회원 id 값은 필수입니다.");
        } else{
            jpql += " where t.member.id = :memberId";
        }
        //검색 기간(month or week or day)
        if (todoSearch.getStartDate() != null && todoSearch.getEndDate() != null) {
            jpql += " and tt.startTime between :startDate and :endDate";
        } // startDate가 비어있는 경우는 없다.
        else if(todoSearch.getStartDate() == null && todoSearch.getEndDate() != null){
            throw new IllegalArgumentException("끝 날짜는 존재하지만 시작 날짜가 존재하지 않습니다.");
        }
        else if(todoSearch.getStartDate() != null && todoSearch.getEndDate() == null){
            throw new IllegalArgumentException("시작 날짜는 존재하지만 끝 날짜가 존재하지 않습니다.");
        }

        TypedQuery<Todo> query = em.createQuery(jpql, Todo.class);
        query = query.setParameter("memberId", todoSearch.getMemberId());
        if (todoSearch.getStartDate() != null) {
            query = query.setParameter("startDate", todoSearch.getStartDate());
            query = query.setParameter("endDate", todoSearch.getEndDate());
        }
        return query.getResultList();
    }
}
