package HwanKim.SpringToDo.controller.Task;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.exception.WrongDataAccessException;
import HwanKim.SpringToDo.repository.TaskRepository;
import HwanKim.SpringToDo.service.MemberService;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.session.SessionModules;
import HwanKim.SpringToDo.session.SessionStrings;
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

    @GetMapping("/tasks")
    public String getAll(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "home";
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
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "home";
        }
        model.addAttribute("taskForm", new TaskForm());
        return "task/newTaskForm";
    }

    @PostMapping("/tasks/new")
    public String create(Model model, @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
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

    // url상에 taskId가 넘어오므로, service단에서 사용자 권한 체크 필요
    @GetMapping("/tasks/{taskId}/update")
    public String updateTaskForm(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "home";
        }
        try{
            Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
            taskService.checkAuth(loginId, taskId);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "home";
        }

        TaskDTO task = taskService.findOneById(taskId);
        model.addAttribute("taskForm", task);
        return "task/updateTaskForm";
    }

    @PostMapping("/tasks/{taskId}/update")
    public String update(@PathVariable("taskId") Long taskId, Model model,
                         @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "home";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            taskService.checkAuth(loginId, taskId);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "home";
        }

        TaskDTO taskBeforeUpdated = taskService.findOneById(taskId);
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
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "home";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            taskService.checkAuth(loginId, taskId);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "home";
        }


        taskService.delete(taskId);
        return "redirect:/tasks";
    }
}
