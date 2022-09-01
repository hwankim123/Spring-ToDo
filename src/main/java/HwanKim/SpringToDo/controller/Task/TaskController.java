package HwanKim.SpringToDo.controller.Task;

import HwanKim.SpringToDo.DTO.TaskDto;
import HwanKim.SpringToDo.DTO.UserDto;
import HwanKim.SpringToDo.config.auth.CustomOAuth2UserService;
import HwanKim.SpringToDo.config.auth.LoginUser;
import HwanKim.SpringToDo.config.auth.dto.SessionUser;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final CustomOAuth2UserService userService;
    private final TaskService taskService;

    /**
     * 로그인 정보를 확인한 후
     * 세션의 로그인한 사용자 id로 사용자가 생성한 작업 목록을 모두 조회
     */
    @GetMapping
    public String getAll(Model model, @LoginUser SessionUser user){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks", "TaskController", "getAll");

        List<TaskDto> tasks = taskService.findAllByUserId(user.getId());
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
    @GetMapping("/new")
    public String newTaskForm(Model model){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks/new", "TaskController", "newTaskForm");

        model.addAttribute("taskForm", new TaskForm());
        return "/task/newTaskForm";
    }

    /**
     * 로그인 정보를 확인한 후
     * View 계층에서의 validation : 작업 이름을 입력하지 않은 경우 작업 생성 재진행
     * View 계층에서의 validation을 통과했다면 작업 생성 로직을 진행하며 Service 계층에서의 validation 진행
     */
    @PostMapping("/new")
    public String create(Model model, @Valid TaskForm taskForm, BindingResult result, @LoginUser SessionUser user){
        log.info("mapped url '{}'. {}.{}() method called.", "/tasks/new", "TaskController", "create");

        if(result.hasErrors()){
            return "/task/newTaskForm";
        }

        UserDto userDto = userService.findUser(user.getId());
        TaskDto newTaskDto = TaskDto.builder()
                .userDto(userDto)
                .name(taskForm.getName())
                .desc(taskForm.getDesc())
                .build();
        try{
            taskService.saveTask(newTaskDto);
        } catch(TaskNameDuplicateException e){
            log.info("TaskNameDuplicatedException occurred.");
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
    @GetMapping("/{taskId}/update")
    public String updateTaskForm(@PathVariable("taskId") Long taskId, Model model, @LoginUser SessionUser user){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/update", "TaskController", "getAll");

        TaskDto task = taskService.findOneById(user.getId(), taskId);
        model.addAttribute("taskForm", task);
        return "/task/updateTaskForm";
    }

    /**
     * 로그인 정보를 확인한 후 로그인한 사용자가 해당 작업을 수정할 권한이 있는지 검증
     * 검증한 후 View 계층에서의 validation 진행 : 작업 이름을 작성하지 않은 경우 작업 수정 재진행
     * View 계층에서의 validation을 통과했다면 작업 수정 로직을 진행하여 Service 계층에서의 validation 진행
     */
    @PostMapping("/{taskId}/update")
    public String update(@PathVariable("taskId") Long taskId, Model model,
                         @Valid TaskForm taskForm, BindingResult result, HttpServletRequest request, @LoginUser SessionUser user){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/update", "TaskController", "update");

        if(result.hasErrors()){
            model.addAttribute("taskForm", taskForm);
            return "/task/updateTaskForm";
        }

        try{
            TaskDto updatedTask = TaskDto.builder()
                    .id(taskId)
                    .name(taskForm.getName())
                    .desc(taskForm.getDesc())
                    .build();
            taskService.update(user.getId(), updatedTask);
        } catch(TaskNameDuplicateException e){
            log.info("TaskNameDuplicatedException occurred.");
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
    @DeleteMapping("/{taskId}/delete")
    public String delete(@PathVariable("taskId") Long taskId, Model model, HttpServletRequest request, @LoginUser SessionUser user){
        log.info("mapped url '{}{}{}'. {}.{}() method called.", "/tasks/", taskId, "/delete", "TaskController", "delete");

        taskService.delete(user.getId(), taskId);

        log.info("TaskController.delete() : all delete exceptions passed. redirect to '/tasks'");

        return "redirect:/tasks";
    }
}
