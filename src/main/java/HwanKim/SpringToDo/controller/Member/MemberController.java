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

    /**
     * 회원 가입 form 화면을 return
     */
    @GetMapping("/member/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    /**
     * View 계층에서의 validation : 사용자가 잘못된 형식의 정보를 입력했거나, 필수 입력 사항을 입력하지 않은 경우 회원 가입 재진행
     * View 계층에서의 validation을 통과하면 회원가입 로직을 진행하며 Service 계층에서의 validation 진행
     * Service 계층에서의 validation을 통과하면 index 페이지로 redirect
     */
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

    /**
     * 로그인 form 화면을 return
     */
    @GetMapping("/member/login")
    public String loginForm(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    /**
     * View 계층에서의 validation : 아이디 혹은 비밀번호를 입력하지 않은 경우 로그인 재진행
     * View 계층에서의 validation을 통과했다면 로그인 로직을 진행하며 Service 계층에서의 validation 진행
     */
    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, HttpServletRequest request){

        // View 계층에서의 validation
        if(result.hasErrors()){
            return "member/loginForm";
        }

        // Service 계층에서의 validation
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

    /**
     * 세션의 전체 데이터를 삭제하며 로그아웃
     */
    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    /**
     * 세션을 통해 로그인 정보를 확인한 후
     * model에 사용자의 이름과 id 정보를 추가
     */
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
