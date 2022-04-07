package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    // memberForm에 Valid 자바 기능을 사용하기 때문에, 편리하게 valid 기능을 사용할 수 있게 된다.
    // 이 말을 무었이냐, memberService에서 우리가 해줬던 valid 함수들의 필요성에 대해서 재점검할 필요가 있다.
    @PostMapping("/member/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){

        if(result.hasErrors()){
            return "member/createMemberForm";
        }
        Member member = new Member(memberForm.getName(), memberForm.getUsername(), memberForm.getPassword());

        memberService.signUp(member);
        return "redirect:/";
    }
}
