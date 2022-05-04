package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.MemberDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.exception.WrongPasswordException;
import HwanKim.SpringToDo.exception.WrongUsernameException;
import HwanKim.SpringToDo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * 중복 username 검증
     * 비밀번호는 최소 6자리
     */
    public Long signUp(MemberDTO memberDTO){
        Member member = new Member(memberDTO.getName(), memberDTO.getUsername(), memberDTO.getPassword());
        validateUsername(member);
        validatePassword(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복 username 검증
    private void validateUsername(Member member){
        List<Member> findMembers = memberRepository.findByUsername(member.getUsername());
        if(!findMembers.isEmpty()){
            throw new WrongUsernameException("이미 존재하는 회원입니다.");
        }
    }

    // 비밀번호 검증 - 최소 6자리
    private void validatePassword(Member member){
        if(member.getPassword().length() < 6){
            throw new WrongPasswordException("비밀번호는 최소 6자리입니다.");
        }
    }

    /**
     * 로그인
     */
    public MemberDTO login(String username, String password){
        List<Member> findMembers = memberRepository.findByUsername(username);
        if(findMembers.isEmpty()){
            throw new WrongUsernameException("아이디가 틀립니다.");
        }
        if(!findMembers.get(0).getPassword().equals(password)){
            throw new WrongPasswordException("비밀번호가 틀립니다.");
        }
        return new MemberDTO(
                findMembers.get(0).getId(),
                findMembers.get(0).getName(),
                findMembers.get(0).getUsername(),
                findMembers.get(0).getPassword()
        );
    }

    /**
     * 사용자 조회(마이페이지)
     */
    // 리팩토링 필요 : return을 MemberDTO로 하도록
    @Transactional(readOnly = true)
    public Member findMember(Long memberId){
        return memberRepository.findById(memberId);
    }

    /**
     * 회원 탈퇴
    */
    public void withdraw(Member member){
        memberRepository.delete(member);
    }
}
