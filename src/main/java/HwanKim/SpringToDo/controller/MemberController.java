package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.exception.WrongPasswordException;
import HwanKim.SpringToDo.exception.WrongUsernameException;
import HwanKim.SpringToDo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.WrongClassException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
            System.out.println(result.getAllErrors().toString());
            return "member/createMemberForm";
        }
        Member member = new Member(memberForm.getName(), memberForm.getUsername(), memberForm.getPassword());

        try{
            memberService.signUp(member);
        } catch(WrongUsernameException e){
            result.addError(new FieldError("memberForm", "username", e.getMessage()));
            return "member/createMemberForm";
        } catch(WrongPasswordException e){
            result.addError(new FieldError("memberForm", "password", e.getMessage()));
            return "member/createMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    // Todo : Session 처리 -> 로그인 유지 필요
    // https://devkingdom.tistory.com/9
    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult result){

        if(result.hasErrors()){
            return "member/loginForm";
        }

        try{
            memberService.login(loginForm.getUsername(), loginForm.getPassword());
        } catch(WrongUsernameException e){
            result.addError(new FieldError("loginForm", "username", e.getMessage()));
            return "member/loginForm";
        } catch(WrongPasswordException e){
            result.addError(new FieldError("loginForm", "password", e.getMessage()));
            return "member/loginForm";
        }
        return "redirect:/";
    }
}
