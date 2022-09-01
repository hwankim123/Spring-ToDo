package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.DTO.TaskDto;
import HwanKim.SpringToDo.DTO.UserDto;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.domain.User;
import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
import HwanKim.SpringToDo.repository.TaskRepository;
import HwanKim.SpringToDo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 전체 Task를 조회한 후 return
     */
    @Transactional(readOnly = true)
    public List<TaskDto> findAll(Long memberId){
        List<Task> tasks = taskRepository.findByMemberId(memberId);
        List<TaskDto> taskDtos = new ArrayList<>();
        for(Task t : tasks){
            taskDtos.add(new TaskDto(t));
        }
        return taskDtos;
    }

    /**
     * 사용자의 전체 Task를 조회한 후 return
     */
    @Transactional(readOnly = true)
    public List<TaskDto> findAllByUserId(Long userId){
        return taskRepository.findByUserId(userId).stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 전체 Task중 task id가 일치하는 작업을 return
     */
    @Transactional(readOnly = true)
    public TaskDto findOneById(Long userId, Long taskId){
        Task task = taskRepository.findById(userId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));

        return new TaskDto(task);
    }

    /**
     * 사용자의 전체 Task중 이름이 일치하는 작업을 return
     */
    @Transactional(readOnly = true)
    public TaskDto findOneByName(Long userId, String name){

        return taskRepository.findByNameInUser(name, userId).stream()
                .findFirst()
                .map(TaskDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
    }

    /**
     * Task 저장
     * 사용자의 작업 목록중 중복된 이름의 작업이 있는지 validate한 후 작업 생성
     */
    public Long saveTask(TaskDto taskDto){
        UserDto userDto = taskDto.getUserDto();
        validateName(userDto.getId(), taskDto.getName(), null);

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Task task = Task.create(user, taskDto.getName(), taskDto.getDesc());
        taskRepository.save(task);

        return task.getId();
    }

    /**
     * 요청을 보낸 사용자의 작업 목록 중 Task 수정
     */
    public void update(Long userId, TaskDto updatedTask){
        Task pastTask = taskRepository.findById(userId, updatedTask.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
        validateName(userId, updatedTask.getName(), pastTask.getName());

        pastTask.update(updatedTask.getName(), updatedTask.getDesc());
    }

    /**
     * Member별로 생성한 작업 이름은 unique하다.
     * validate 결과 중복된 작업 이름이 조회된 경우 예외처리
     * 작업 생성 시에는 중복된 작업 이름이 조회되지 않아야 하지만,
     * 작업 수정 시에는 아직 수정되기 전 이름이 조회될 수 있으므로, 생성과 수정의 경우를 모두 고려하여 validate
     */
    private void validateName(Long userId, String name, String nameBeforeUpdated) {
        List<Task> tasks = taskRepository.findByNameInUser(name, userId);
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
    public void delete(Long userId, Long taskId){
        Task task = taskRepository.findById(userId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
        taskRepository.remove(task);
    }
}
