package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member("김환", "hwankim123", "cjsak123!");

        //when
        Long signUpId = memberService.signUp(member);

        //then
        assertThat(signUpId).isEqualTo(member.getId());
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member("김환", "hwankim123", "cjsak123!");
        Member member2 = new Member("김환2", "hwankim123", "cjsak123!");

        //when
        Long signUpId1 = memberService.signUp(member1);
        try{
            Long signUpId2 = memberService.signUp(member2);
        } catch(IllegalArgumentException e){
            return;
        }

        //then
        fail("예외가 발생해야 한다.");
    }

    @Test
    public void 로그인() throws Exception{
        //given

        //when

        //then

    }

}