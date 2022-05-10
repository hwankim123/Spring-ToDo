package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.auth.SessionStrings;
import HwanKim.SpringToDo.controller.Task.TaskForm;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.auth.AuthModules;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final TaskService taskService;
    private final AuthModules authModules;

    private final DefaultListableBeanFactory bf;

    @GetMapping("/todo/new")
    public String newTodo(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        List<TaskDTO> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);
        TodoForm todoForm = new TodoForm();
        for(int i = 0; i < 3; i++){
            todoForm.addTask(new TaskDTO(null, null, null));
        }
        model.addAttribute("todoForm", todoForm);

        return "/todo/newTodoForm";
    }

    @PostMapping("/todo/new")
    public String create(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);

        return "/todo";
    }
}
