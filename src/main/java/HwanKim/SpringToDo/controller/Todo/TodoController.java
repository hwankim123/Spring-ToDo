package HwanKim.SpringToDo.controller.Todo;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.DTO.TodoDTO;
import HwanKim.SpringToDo.DTO.TodoTaskDTO;
import HwanKim.SpringToDo.auth.SessionStrings;
import HwanKim.SpringToDo.domain.TodoTaskStatus;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.auth.AuthModules;
import HwanKim.SpringToDo.exception.TodoAlreadyExistException;
import HwanKim.SpringToDo.exception.TodoNotExistException;
import HwanKim.SpringToDo.exception.TodoTaskNameNullException;
import HwanKim.SpringToDo.service.TaskService;
import HwanKim.SpringToDo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;
    private final TaskService taskService;
    private final AuthModules authModules;

    /**
     * 로그인 정보를 확인한 후
     * 화면 구성을 위해 로그인한 사용자의 모든 작업 목록과 할일 작성 form 클래스를 model에 추가
     * Service 계층에서의 validation 결과 이미 오늘의 할일이 작성되어있는 경우 오늘의 할일 페이지로 redirect
     * 할일 작성 form 화면을 return
     */
    @GetMapping("/new")
    public String newTodo(Model model, HttpServletRequest request, RedirectAttributes rtt){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/new", "TodoController", "newTodo");

        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            todoService.validateTodoAlreadyExist(loginId);
        } catch(TodoAlreadyExistException e){
            log.info("TodoAlreadyExistException occurred. redirect to '/todo/today'");
            rtt.addFlashAttribute("isTodoAlreadyExist", true);
            return "redirect:/todo/today";
        }
        List<TaskDTO> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("todoForm", new TodoForm());

        // 사용자가 입력한 개행 문자를 View 상에도 적용시키기 위해 java에서 제공하는 개행문자를 model에 추가
        String nlString = System.getProperty("line.separator");
        model.addAttribute("nlString", nlString);
        return "todo/newTodoForm";
    }

    /**
     * 로그인 정보를 확인한 후
     * 할일 작성 로직을 실행하여 Service 계층에서의 validation 진행
     * Service 계층에서의 validation 결과 할일의 작업 list중 작업 이름이 비어있는 경우 할일 재작성
     * validation을 마친 후 오늘의 할일 페이지로 redirect
     */
    @PostMapping("/new")
    public String create(Model model, @Valid TodoForm todoForm, BindingResult result, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/new", "TodoController", "create");

        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }

        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);

        if(result.hasErrors()){
            result.addError(new ObjectError("table", "작업을 하나 이상 등록해주세요."));
            return "todo/newTodoForm";
        }

        String[] names = todoForm.getNames();
        String[] descs = todoForm.getDescs();
        try{
            todoService.saveTodo(loginId, names, descs);
        } catch(TodoTaskNameNullException e){
            log.info("TodoTaskNameNullException occurred. redirect to '/todo/new'");
            result.addError(new ObjectError("table", e.getMessage()));

            return "todo/newTodoForm";
        }
        log.info("all exceptions passed. redirect to '/todo/today'");
        return "redirect:/todo/today";
    }

    /**
     * 로그인 정보를 확인한 후
     * 작성한 할일과 작업의 완료 여부를 model에 추가
     * 오늘의 할일 페이지를 return
     */
    @GetMapping("/today")
    public String getTodaysTodo(Model model, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/today", "TodoController", "getTodaysTodo");
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            todoService.validateTodoNotExist(loginId);
        } catch(TodoNotExistException e){
            log.info("TodoNotExistException occurred.");
            model.addAttribute("isTodoNotExist", true);
            return "todo/today";
        }

        TodoDTO todaysTodo = todoService.findTodaysTodo(loginId);
        model.addAttribute("todaysTodo", todaysTodo);
        model.addAttribute("todoTaskStatus", TodoTaskStatus.values());

        // 사용자가 입력한 개행 문자를 View 상에도 적용시키기 위해 java에서 제공하는 개행문자를 model에 추가
        String nlString = System.getProperty("line.separator");
        model.addAttribute("nlString", nlString);

        return "todo/today";
    }

    @GetMapping("/today/update")
    public String updateForm(Model model, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/today/update", "TodoController", "updateForm");
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);
        try{
            todoService.validateTodoNotExist(loginId);
        } catch(TodoNotExistException e){
            log.info("TodoNotExistException occurred.");
            model.addAttribute("isTodoNotExist", true);
            return "todo/updateForm";
        }

        List<TaskDTO> tasks = taskService.findAll(loginId);
        model.addAttribute("tasks", tasks);

        TodoDTO todaysTodo = todoService.findTodaysTodo(loginId);
        List<TodoTaskDTO> todoTasks = todaysTodo.getTodoTasks();

        TodoForm todoForm = new TodoForm();
        todoForm.setIds(todoTasks.stream()
                .map(TodoTaskDTO::getId).toArray(Long[]::new));
        todoForm.setNames(todoTasks.stream()
                .map(TodoTaskDTO::getName).toArray(String[]::new));
        todoForm.setDescs(todoTasks.stream()
                .map(TodoTaskDTO::getDesc).toArray(String[]::new));

        model.addAttribute("todoForm", todoForm);

        // 사용자가 입력한 개행 문자를 View 상에도 적용시키기 위해 java에서 제공하는 개행문자를 model에 추가
        String nlString = System.getProperty("line.separator");
        model.addAttribute("nlString", nlString);

        return "todo/updateForm";
    }

    @PostMapping("/today/update")
    public String update(Model model, @Valid TodoForm todoForm, BindingResult result, HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/today/update", "TodoController", "updateForm");
        HttpSession session = request.getSession();
        try{
            authModules.checkSession(session);
        } catch(SessionInvalidException e){
            model.addAttribute(e.getMessage(), true);
            return "/exceptions";
        }
        Long loginId = (Long) session.getAttribute(SessionStrings.SESSION_ID);

        if(result.hasErrors()){
            result.addError(new ObjectError("table", "작업을 하나 이상 등록해주세요."));
            return "todo/updateForm";
        }

        try{
            todoService.update(loginId, todoForm);
        } catch(TodoTaskNameNullException e){
            log.info("TodoTaskNameNullException occurred. redirect to '/todo/update'");
            result.addError(new ObjectError("table", e.getMessage()));

            return "/todo/updateForm";
        }
        log.info("all exceptions passed. redirect to '/todo/today'");
        return "redirect:/todo/today";
    }

    /**
     * View 계층의 JavaScript fetch api 요청에 대한 응답
     * 완료한 작업의 경우 작업의 상태를 미완료로 변경하여 변경된 데이터를 반환
     * 미완료한 작업의 경우 작업의 상태를 완료로 변경하여 변경된 데이터를 반환
     */
    @ResponseBody
    @PutMapping("/change-status")
    public TodoTaskStatusForm changeStatus(@RequestBody TodoTaskStatusForm todoTaskData) {
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/change-status", "TodoController", "changeStatus");

        TodoDTO todo = todoService.changeStatusOfTodoTask(
                todoTaskData.getTodoId(),
                todoTaskData.getTodoTaskId(),
                todoTaskData.getStatus()
        );

        TodoTaskStatusForm todoTaskStatusForm = new TodoTaskStatusForm();
        for (TodoTaskDTO todoTask : todo.getTodoTasks()) {
            if (todoTask.getId().equals(todoTaskData.getTodoTaskId())) {
                todoTaskStatusForm.setTodoId(todo.getId());
                todoTaskStatusForm.setTodoTaskId(todoTask.getId());
                todoTaskStatusForm.setStatus(todoTask.getStatus());
                break;
            }
        }
        return todoTaskStatusForm;
    }

    /**
     * 오늘의 할일 삭제
     */
    @GetMapping("/delete")
    public String delete(HttpServletRequest request){
        log.info("mapped url '{}'. {}.{}() method called.", "/todo/delete", "TodoController", "delete");
        todoService.delete((Long)(request.getSession().getAttribute(SessionStrings.SESSION_ID)));
        return "/home";
    }
}
