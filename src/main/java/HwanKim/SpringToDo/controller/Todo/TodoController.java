package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.session.SessionModules;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class TodoController {

    @GetMapping("/todo/new")
    public String newTodo(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "home";
        }

        return "todo/newTodoForm";
    }
}
