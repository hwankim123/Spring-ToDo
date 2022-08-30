package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.auth.SessionStrings;
import HwanKim.SpringToDo.config.auth.LoginUser;
import HwanKim.SpringToDo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final HttpSession httpSession;

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest request, @LoginUser SessionUser user){
        log.info("mapped url '{}'. {}.{}() method called.", "/", "HomeController", "home");

        if(user != null){
            model.addAttribute("loginName", user.getName());
        }
//        model.addAttribute("loginId", session.getAttribute(SessionStrings.SESSION_ID));
//        model.addAttribute("loginName", session.getAttribute(SessionStrings.SESSION_NAME));
        return "home";
    }
}
