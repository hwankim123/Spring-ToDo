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
     * 사용자의 전체 Task를 조회한 후 return
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
     * 사용자의 전체 Task중 task id가 일치하는 작업을 return
     */
    public TaskDTO findOneById(Long memberId, Long taskId){

        Task task = taskRepository.findById(memberId, taskId);
        return new TaskDTO(task);
    }

    /**
     * 사용자의 전체 Task중 이름이 일치하는 작업을 return
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
     * 사용자의 작업 목록중 중복된 이름의 작업이 있는지 validate한 후 작업 생성
     */
    public Long saveTask(TaskDTO taskDTO){
        Member member = taskDTO.getMember();
        validateName(member.getId(), taskDTO.getName(), null);
        Task task = Task.create(member, taskDTO.getName(), taskDTO.getDesc());
        taskRepository.save(task);
        return task.getId();
    }

    /**
     * 요청을 보낸 사용자의 작업 목록 중 Task 수정
     */
    public void update(Long memberId, TaskDTO updatedTask){
        TaskDTO pastTask = findOneById(memberId, updatedTask.getId());

        validateName(memberId, updatedTask.getName(), pastTask.getName());

        Task task = taskRepository.findById(memberId, updatedTask.getId());
        task.update(updatedTask.getName(), updatedTask.getDesc());
    }

    /**
     * Member별로 생성한 작업 이름은 unique하다.
     * validate 결과 중복된 작업 이름이 조회된 경우 예외처리
     * 작업 생성 시에는 중복된 작업 이름이 조회되지 않아야 하지만,
     * 작업 수정 시에는 아직 수정되기 전 이름이 조회될 수 있으므로, 생성과 수정의 경우를 모두 고려하여 validate
     */
    private void validateName(Long memberId, String name, String nameBeforeUpdated) {
        List<Task> tasks = taskRepository.findByNameInMember(name, memberId);
        if(tasks.size() != 0){
            // 수정 전 이름이 파라미터로 전달되지 않은 경우(생성 작업), 혹은 동일한 이름의 작업이 조회된 경우 예외처리
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
