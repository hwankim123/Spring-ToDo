package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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

    public Optional<Task> findById(Long userId, Long id){ // memberID -> userId 수정
        return em.createQuery("select t from Task t where t.user.id = :userId and t.id = :id", Task.class)
                .setParameter("userId", userId)
                .setParameter("id", id)
                .getResultList()
                .stream().findFirst();
    }

    public List<Task> findByMemberId(Long memberId){
        return em.createQuery("select t from Task t where t.member.id = :memberId", Task.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<Task> findByUserId(Long userId){
        return em.createQuery("select t from Task t where t.user.id = :userId", Task.class)
                .setParameter("userId", userId)
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

    public List<Task> findByNameInUser(String name, Long userId){
        return em.createQuery("select t from Task t where t.user.id = :userId and t.name = :name", Task.class)
                .setParameter("userId", userId)
                .setParameter("name", name)
                .getResultList();
    }

    public void remove(Task task){
        em.remove(task);
    }
}
