package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final EntityManager em;

    public void save(Task task){
        if(task.getId() == null){
            em.persist(task);
        } else{
            System.out.println("merge task");
            em.merge(task);
        }
    }

    public Task findById(Long memberId, Long id){
        return em.createQuery("select t from Task t where t.member.id = :memberId and t.id = :id", Task.class)
                .setParameter("memberId", memberId)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Task> findByMemberId(Long memberId){
        return em.createQuery("select t from Task t where t.member.id = :memberId", Task.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Task> findByName(String name){
        return em.createQuery("select t from Task t where t.name = :name", Task.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Task> findByNameInMember(String name, Long memberId){
        return em.createQuery("select t from Task t where t.member.id = :memberId and t.name = :name", Task.class)
                .setParameter("memberId", memberId)
                .setParameter("name", name)
                .getResultList();
    }

    public void remove(Task task){
        em.remove(task);
    }
}
