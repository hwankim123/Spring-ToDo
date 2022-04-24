package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.controller.TaskForm;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.repository.MemberRepository;
import HwanKim.SpringToDo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    /**
     * Task 저장
     * task의 name은 member별로 unique
     */
    public Long saveTask(TaskDTO taskDTO){
        Member member = taskDTO.getMember();
        validateName(member.getId(), taskDTO.getName());
        Task task = Task.create(member, taskDTO.getName(), taskDTO.getName());
        taskRepository.save(task);
        return task.getId();
    }

    // Member별로 생성한 Task의 중복 name validate
    private void validateName(Long memberId, String name) {
        List<Task> tasks = taskRepository.findByNameInMember(name, memberId);
        System.out.println(tasks.size());
        if(tasks.size() != 0){
            throw new TaskNameDuplicateException("작업 이름이 이미 존재합니다.");
        }
    }

    /**
     * Task 전체 검색
     */
    public List<TaskDTO> findAll(Long memberId){
        List<Task> tasks = taskRepository.findByMemberId(memberId);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for(Task t : tasks){
            taskDTOs.add(new TaskDTO(t));
        }
        return taskDTOs;
    }

    /**
     * Task 이름 검색
     */

    public TaskDTO findOneByName(Long memberId, String name){

        List<Task> tasks = taskRepository.findByNameInMember(name, memberId);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for(Task t : tasks){
            taskDTOs.add(new TaskDTO(t));
        }
        return taskDTOs.get(0);
    }

    public TaskDTO findOneById(Long taskId){

        Task task = taskRepository.findById(taskId);
        return new TaskDTO(task);
    }

    /**
     * Task 수정
     */
    public void update(Long memberId, TaskDTO taskDTO){
        validateName(memberId, taskDTO.getName());

        Task task = taskRepository.findById(taskDTO.getId());
        task.update(taskDTO.getName(), taskDTO.getDesc());
    }

    /**
     * Task 삭제
     */
    public void delete(Task task){
        taskRepository.remove(task);
    }

}
