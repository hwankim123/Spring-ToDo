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

    /**
     * TODO: task.getId()로 신규 작업 등록과 기존 작업의 수정 작업의 id 존재 여부를 확인해볼 것
     */
    public void save(Task task){
        if(task.getId() == null){
            System.out.println("persist task");
            em.persist(task);
        } else{
            System.out.println("merge task");
            em.merge(task);
        }
    }

    public Task findById(Long id){
        return em.find(Task.class, id);
    }

    public List<Task> findByMemberId(Long memberId){
        return em.createQuery("select t from Task t inner join Member m on m.id = :memberId", Task.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    /**
     * find by name
     * ToDo: name에 Unique 제약 조건을 달지 안달지에 따라 return type 수정 필요
     */
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
