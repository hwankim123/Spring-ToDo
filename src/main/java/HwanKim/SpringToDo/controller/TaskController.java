package HwanKim.SpringToDo.controller;

import HwanKim.SpringToDo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public String getTasks(Model model){
        return "/tasks";
    }

    @PostMapping("/tasks/new")
    public String createTask(@Valid TaskForm taskForm, BindingResult result){
        //TODO1 : 세선 데이터 확인하는 함수 모듈화
        //TODO2 : TaskForm 만들어야함
        //TODO3 : TaskService.saveTask의 파라미터를 memberId, name, desc에서 TaskDTO로 바꿔야 함
        return "redirect:/tasks";
    }
}
