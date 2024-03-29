//package HwanKim.SpringToDo.service;
//
//import HwanKim.SpringToDo.DTO.MemberDTO;
//import HwanKim.SpringToDo.domain.Member;
//import HwanKim.SpringToDo.exception.WrongPasswordException;
//import HwanKim.SpringToDo.exception.WrongUsernameException;
//import HwanKim.SpringToDo.repository.MemberRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class MemberServiceTest {
//
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Rollback(false)
//    public void 회원가입() throws Exception{
//        //given
//        Member member = new Member("김환", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//        //when
//        Long signUpId = memberService.signUp(memberDTO);
//
//        //then
//        assertThat(signUpId).isEqualTo(member.getId());
//    }
//
//    @Test
//    public void 중복아이디_회원가입_예외() throws Exception{
//        //given
//        Member member1 = new Member("김환", "hwankim123", "cjsak123!");
//        Member member2 = new Member("김환2", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO1 = new MemberDTO(member1.getId(), member1.getName(), member1.getUsername(), member1.getPassword());
//        MemberDTO memberDTO2 = new MemberDTO(member2.getId(), member2.getName(), member2.getUsername(), member2.getPassword());
//
//        //when
//        Long signUpId1 = memberService.signUp(memberDTO1);
//        try{
//            Long signUpId2 = memberService.signUp(memberDTO2);
//        } catch(WrongUsernameException e){
//            return;
//        }
//
//        //then
//        fail("중복 회원 예외가 발생해야 한다.");
//    }
//
//    @Test
//    public void 비밀번호_자릿수_회원가입_예외() throws Exception{
//        //given
//        Member member = new Member("김환", "hwa", "asd");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//
//        //when
//        try{
//            Long findMember = memberService.signUp(memberDTO);
//        } catch(WrongPasswordException e){
//            return;
//        }
//
//        //then
//        fail("비밀번호 자릿수 예외가 발생해야 한다.");
//    }
//
//    @Test
//    public void 로그인() throws Exception{
//        //given
//        Member member = new Member("김환", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//
//        System.out.println("===singUp===");
//        Long newMemberId = memberService.signUp(memberDTO);
//        System.out.println("============");
//
//        //when
//        Member findMember = memberRepository.findById(newMemberId);
//        System.out.println("findMember.getUsername() = " + findMember.getUsername() + " findMember.getPassword() = " + findMember.getPassword());
//        System.out.println("===LogIn===");
//        MemberDTO loginMember = memberService.login(findMember.getUsername(), findMember.getPassword());
//        System.out.println("===========");
//
//        //then
//        assertThat(loginMember.getId()).isEqualTo(findMember.getId());
//    }
//
//    @Test
//    public void 아이디_불일치_로그인_예외() throws Exception{
//        //given
//        Member member = new Member("김환", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//
//        System.out.println("===singUp===");
//        Long newMemberId = memberService.signUp(memberDTO);
//        System.out.println("============");
//
//        //when
//        Member findMember = memberRepository.findById(newMemberId);
//        System.out.println("findMember.getId() = " + findMember.getId());
//
//        //then
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            memberService.login("XXX_Wrong_Username", findMember.getPassword());
//        });
//    }
//
//    @Test
//    public void 비밀번호_불일치_로그인_예외() throws Exception{
//        //given
//        Member member = new Member("김환", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//
//        System.out.println("===singUp===");
//        Long newMemberId = memberService.signUp(memberDTO);
//        System.out.println("============");
//
//        //when
//        Member findMember = memberRepository.findById(newMemberId);
//        System.out.println("findMember.getId() = " + findMember.getId());
//
//        //then
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            memberService.login(findMember.getUsername(), "XXX_Wrong_Password");
//        });
//    }
//}