package HwanKim.SpringToDo.controller.Member;

import HwanKim.SpringToDo.DTO.MemberDTO;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.WrongPasswordException;
import HwanKim.SpringToDo.exception.WrongUsernameException;
import HwanKim.SpringToDo.service.MemberService;
import HwanKim.SpringToDo.auth.AuthModules;
import HwanKim.SpringToDo.auth.SessionStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthModules authModules;

    @GetMapping("/member/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){

        // View 계층에서의 validation
        if(result.hasErrors()){
            return "member/createMemberForm";
        }
        MemberDTO memberDTO = new MemberDTO(null, memberForm.getName(), memberForm.getUsername(), memberForm.getPassword());

        // Service 계층에서의 validation
        try{
            memberService.signUp(memberDTO);
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

    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, HttpServletRequest request){

        if(result.hasErrors()){
            return "member/loginForm";
        }

        MemberDTO loginMemberDTO;
        try{
            loginMemberDTO = memberService.login(loginForm.getUsername(), loginForm.getPassword());
        } catch(WrongUsernameException e){
            result.addError(new FieldError("loginForm", "username", e.getMessage()));
            return "member/loginForm";
        } catch(WrongPasswordException e){
            result.addError(new FieldError("loginForm", "password", e.getMessage()));
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionStrings.SESSION_ID, loginMemberDTO.getId());
        session.setAttribute(SessionStrings.SESSION_NAME, loginMemberDTO.getName());
        session.setAttribute(SessionStrings.SESSION_USERNAME, loginMemberDTO.getUsername());

        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    @GetMapping("/member/mypage")
    public String mypage(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        model.addAttribute("name", session.getAttribute(SessionStrings.SESSION_NAME));
        model.addAttribute("username", session.getAttribute(SessionStrings.SESSION_USERNAME));
        return "member/mypage";
    }
}
