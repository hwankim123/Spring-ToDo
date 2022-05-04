package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.exception.WrongDataAccessException;
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
     * Task id 검색
     */
    public TaskDTO findOneById(Long memberId, Long taskId){

        Task task = taskRepository.findById(memberId, taskId);
        return new TaskDTO(task);
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

    /**
     * Task 저장
     * task의 name은 member별로 unique
     */
    public Long saveTask(TaskDTO taskDTO){
        Member member = taskDTO.getMember();
        validateName(member.getId(), taskDTO.getName(), null);
        Task task = Task.create(member, taskDTO.getName(), taskDTO.getDesc());
        taskRepository.save(task);
        return task.getId();
    }

    /**
     * Task 수정
     */
    public void update(Long memberId, TaskDTO taskDTO, String nameBeforeUpdated){
        validateName(memberId, taskDTO.getName(), nameBeforeUpdated);

        Task task = taskRepository.findById(memberId, taskDTO.getId());
        task.update(taskDTO.getName(), taskDTO.getDesc());
    }

    /**
     * Member별로 생성한 Task의 중복 name validate
     */
    private void validateName(Long memberId, String name, String nameBeforeUpdated) {
        List<Task> tasks = taskRepository.findByNameInMember(name, memberId);
        if(tasks.size() != 0){
            if(nameBeforeUpdated == null || !nameBeforeUpdated.equals(tasks.get(0).getName())){
                throw new TaskNameDuplicateException("작업 이름이 이미 존재합니다.");
            }
        }
    }

    /**
     * Task 삭제
     */
    public void delete(Long memberId, Long taskId){
        Task task = taskRepository.findById(memberId, taskId);
        taskRepository.remove(task);
    }
}
