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

    public void save(Member member){
        em.persist(member);
    }

    public Member findById(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUsername(String username){
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public void delete(Member member){
        em.remove(member);
    }
}
