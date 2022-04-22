package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.service.MemberService;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.session.SessionModules;
import HwanKim.SpringToDo.session.SessionStrings;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final MemberService memberService;
    private final TaskService taskService;

    @GetMapping("/tasks")
    public String getTasks(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "home";
        }
        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        List<Task> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);
        return "/task/taskList";
    }

    @GetMapping("/tasks/new")
    public String newTaskForm(Model model){
        model.addAttribute("taskForm", new TaskForm());
        return "task/newTaskForm";
    }
    @PostMapping("/tasks/new")
    public String createTask(Model model, @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "home";
        }

        if(result.hasErrors()){
            return "task/newTaskForm";
        }

        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        Member member = memberService.findMember(loginId);
        TaskDTO newTaskDTO = new TaskDTO(member, taskForm.getName(), taskForm.getDesc());
        try{
            taskService.saveTask(newTaskDTO);
        } catch(TaskNameDuplicateException e){
            result.addError(new FieldError("taskForm", "name", e.getMessage()));
            return "task/newTaskForm";
        }

        return "redirect:/tasks";
    }
}
