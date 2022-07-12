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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {

    private final MemberService memberService;
    private final TaskService taskService;
    private final AuthModules authModules;

    /**
     * 로그인 정보를 확인한 후
     * 세션의 로그인한 사용자 id로 사용자가 생성한 작업 목록을 모두 조회
     */
    @GetMapping("/tasks")
    public String getAll(Model model, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks", "TaskController", "getAll");

        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }
        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        List<TaskDTO> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);

        // 사용자가 입력한 개행 문자를 View 상에도 적용시키기 위해 java에서 제공하는 개행문자를 model에 추가
        String nlString = System.lineSeparator();
        model.addAttribute("nlString", nlString);
        return "/task/taskList";
    }

    /**
     * 로그인 정보를 확인한 후
     * 새로운 task 생성을 위한 form 화면을 return
     */
    @GetMapping("/tasks/new")
    public String newTaskForm(Model model, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks/new", "TaskController", "newTaskForm");

        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }
        model.addAttribute("taskForm", new TaskForm());
        return "/task/newTaskForm";
    }

    /**
     * 로그인 정보를 확인한 후
     * View 계층에서의 validation : 작업 이름을 입력하지 않은 경우 작업 생성 재진행
     * View 계층에서의 validation을 통과했다면 작업 생성 로직을 진행하며 Service 계층에서의 validation 진행
     */
    @PostMapping("/tasks/new")
    public String create(Model model, @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks/new", "TaskController", "create");

        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }

        if(result.hasErrors()){
            return "/task/newTaskForm";
        }

        Long loginId = ((Long) session.getAttribute(SessionStrings.SESSION_ID));
        Member member = memberService.findMember(loginId); // 리팩토링 필요 : DTO를 반환하도록
        TaskDTO newTaskDTO = new TaskDTO(member, taskForm.getName(), taskForm.getDesc());
        try{
            taskService.saveTask(newTaskDTO);
        } catch(TaskNameDuplicateException e){
            result.addError(new FieldError("taskForm", "name", e.getMessage()));
            return "/task/newTaskForm";
        }

        log.info("TaskController.create() : all exceptions & create validation passed. redirect to '/tasks'");

        return "redirect:/tasks";
    }

    /**
     * 로그인 정보를 확인한 후 로그인한 사용자가 해당 작업을 수정할 권한이 있는지 검증
     * 검증한 후 수정할 작업의 기존 정보를 조회하여 model에 데이터를 추가
     * 작업 수정을 위한 form 화면을 return
     */
    @GetMapping("/tasks/{taskId}/update")
    public String updateTaskForm(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/update", "TaskController", "getAll");

        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            // uri의 path variable로 넘어온 taskId가 현재 로그인한 사용자가 생성한 작업의 id인지 검증
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.UPDATE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "/exceptions";
        }

        TaskDTO task = taskService.findOneById(loginId, taskId);
        model.addAttribute("taskForm", task);
        return "/task/updateTaskForm";
    }

    /**
     * 로그인 정보를 확인한 후 로그인한 사용자가 해당 작업을 수정할 권한이 있는지 검증
     * 검증한 후 View 계층에서의 validation 진행 : 작업 이름을 작성하지 않은 경우 작업 수정 재진행
     * View 계층에서의 validation을 통과했다면 작업 수정 로직을 진행하여 Service 계층에서의 validation 진행
     */
    @PostMapping("/tasks/{taskId}/update")
    public String update(@PathVariable("taskId") Long taskId, Model model,
                         @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/update", "TaskController", "update");

        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            // uri의 path variable로 넘어온 taskId가 현재 로그인한 사용자가 생성한 작업의 id인지 검증
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.UPDATE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "/exceptions";
        }

        TaskDTO taskBeforeUpdated = taskService.findOneById(loginId, taskId);
        if(result.hasErrors()){
            model.addAttribute("taskForm", taskForm);
            return "/task/updateTaskForm";
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
            return "/task/updateTaskForm";
        }

        log.info("TaskController.update() : all exceptions & update validations passed. redirect to '/tasks'");

        return "redirect:/tasks";
    }

    /**
     * 로그인 정보를 확인한 후 로그인한 사용자가 해당 작업을 수정할 권한이 있는지 검증
     * 검증한 후 작업 삭제 진행
     */
    @DeleteMapping("/tasks/{taskId}/delete")
    public String delete(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/delete", "TaskController", "delete");

        HttpSession session = request.getSession();

        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute("sessionInvalid", e.getMessage());
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            // uri의 path variable로 넘어온 taskId가 현재 로그인한 사용자가 생성한 작업의 id인지 검증
            authModules.checkAuthofTask(loginId, taskId, CRUDStatus.DELETE);
        } catch(WrongDataAccessException e){
            model.addAttribute("wrongDataAccess", e.getMessage());
            return "/exceptions";
        }

        taskService.delete(loginId, taskId);

        log.info("TaskController.delete() : all delete exceptions passed. redirect to '/tasks'");

        return "redirect:/tasks";
    }
}
