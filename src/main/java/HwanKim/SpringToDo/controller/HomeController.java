package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.session.SessionStrings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home(Model model, HttpServletRequest request){
        log.info("home controller");
        HttpSession session = request.getSession();
        log.info("session.loginId : " + session.getAttribute(SessionStrings.SESSION_ID));
        log.info("session.loginName : " + session.getAttribute(SessionStrings.SESSION_NAME));
        model.addAttribute("loginId", session.getAttribute(SessionStrings.SESSION_ID));
        model.addAttribute("loginName", session.getAttribute(SessionStrings.SESSION_NAME));
        return "home";
    }
}
