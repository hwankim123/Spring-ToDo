package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.auth.SessionStrings;
import HwanKim.SpringToDo.controller.Task.TaskForm;
import HwanKim.SpringToDo.domain.Todo;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.auth.AuthModules;
import HwanKim.SpringToDo.exception.TodoAlreadyExistException;
import HwanKim.SpringToDo.exception.TodoTaskNameNullException;
import HwanKim.SpringToDo.repository.TodoSearch;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final TaskService taskService;
    private final AuthModules authModules;

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
        model.addAttribute("todoForm", new TodoForm());
        String nlString = System.getProperty("line.separator");
        model.addAttribute("nlString", nlString);
        return "todo/newTodoForm";
    }

    @PostMapping("/todo/new")
    public String create(Model model, @Valid TodoForm todoForm, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }

        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        String[] names = todoForm.getNames();
        String[] descs = todoForm.getDescs();
        try{
            todoService.saveTodo(loginId, names, descs);
        } catch(TodoTaskNameNullException e){
            return "redirect:/todo/new";
        } catch(TodoAlreadyExistException e){
            model.addAttribute(e.getMessage(), true);
            return "redirect:/todo/today";
        }
        return "redirect:/todo/today";
    }

    @GetMapping("/todo/today")
    public String todoPage(Model model, HttpServletRequest request){
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
        Todo todaysTodo = todoService.findTodaysTodo(loginId);
        model.addAttribute("todaysTodo", todaysTodo);
        model.addAttribute("todoForm", new TodoForm());
        String nlString = System.getProperty("line.separator");
        model.addAttribute("nlString", nlString);

        return "todo/today";
    }
}
