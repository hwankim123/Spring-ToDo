package HwanKim.SpringToDo.repository;

import HwanKim.SpringToDo.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * save & update(merge? 그걸로 가능?)
     */
    public void save(Member member){
        em.persist(member);
    }

    /**
     * find by id
     */
    public Member findById(Long id){
        return em.find(Member.class, id);
    }

    /**
     * find by username
     */
    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    /**
     * delete
     */
    public void delete(Member member){
        em.remove(member);
    }
}
