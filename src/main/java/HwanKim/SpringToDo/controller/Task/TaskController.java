package HwanKim.SpringToDo.controller.Task;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.auth.CRUDStatus;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.exception.WrongDataAccessException;
import HwanKim.SpringToDo.service.MemberService;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.auth.AuthModules;
import HwanKim.SpringToDo.auth.SessionStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final MemberService memberService;
    private final TaskService taskService;
    private final AuthModules authModules;

    @GetMapping("/tasks")
    public String getAll(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        List<TaskDTO> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);
        return "/task/taskList";
    }

    @GetMapping("/tasks/new")
    public String newTaskForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        model.addAttribute("taskForm", new TaskForm());
        return "task/newTaskForm";
    }

    @PostMapping("/tasks/new")
    public String create(Model model, @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }

        if(result.hasErrors()){
            return "task/newTaskForm";
        }

        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        Member member = memberService.findMember(loginId); // 리팩토링 필요 : DTO를 반환하도록
        TaskDTO newTaskDTO = new TaskDTO(member, taskForm.getName(), taskForm.getDesc());
        try{
            taskService.saveTask(newTaskDTO);
        } catch(TaskNameDuplicateException e){
            result.addError(new FieldError("taskForm", "name", e.getMessage()));
            return "task/newTaskForm";
        }

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{taskId}/update")
    public String updateTaskForm(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.UPDATE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "exceptions";
        }

        TaskDTO task = taskService.findOneById(loginId, taskId);
        model.addAttribute("taskForm", task);
        return "task/updateTaskForm";
    }

    @PostMapping("/tasks/{taskId}/update")
    public String update(@PathVariable("taskId") Long taskId, Model model,
                         @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.UPDATE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "exceptions";
        }

        TaskDTO taskBeforeUpdated = taskService.findOneById(loginId, taskId);
        if(result.hasErrors()){
            model.addAttribute("taskForm", taskForm);
            return "task/updateTaskForm";
        }

        try{
            TaskDTO updatedTask = new TaskDTO(
                    taskBeforeUpdated.getId(),
                    taskBeforeUpdated.getMember(),
                    taskForm.getName(),
                    taskForm.getDesc());
            taskService.update(loginId, updatedTask, taskBeforeUpdated.getName());
        } catch(TaskNameDuplicateException e){
            result.addError(new FieldError("taskForm", "name", e.getMessage()));
            return "task/updateTaskForm";
        }
        return "redirect:/tasks";
    }

    @DeleteMapping("/tasks/{taskId}/delete")
    public String delete(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.DELETE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "exceptions";
        }


        taskService.delete(loginId, taskId);
        return "redirect:/tasks";
    }
}
