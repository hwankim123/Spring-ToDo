package HwanKim.SpringToDo.controller.Task;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
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
    private final TaskRepository taskRepository;

    @GetMapping("/tasks")
    public String getAll(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
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
            model.addAttribute(e.getMessage(), true);
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

        model.addAttribute("taskForm", new TaskForm());
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{taskId}/update")
    public String updateTaskForm(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "home";
        }

        TaskDTO task = taskService.findOneById(taskId);

        model.addAttribute("taskForm", task);
        return "task/updateTaskForm";
    }

    // ToDo : url 가운데에 {id} 끼워넣기. html도 수정.
    @PostMapping("/tasks/update")
    public String update(Model model, @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        HttpSession session = request.getSession();

        try{
            SessionModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "home";
        }

        TaskDTO taskBeforeUpdated = taskService.findOneById(taskForm.getId());
        if(result.hasErrors()){

            model.addAttribute("taskForm", taskForm);
            return "task/updateTaskForm";
        }

        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
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
            model.addAttribute(e.getMessage(), true);
            return "home";
        }

        taskService.delete(taskId);
        return "redirect:/tasks";
    }
}
