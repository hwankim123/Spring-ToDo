package HwanKim.SpringToDo.service;

import HwanKim.SpringToDo.domain.Member;
import HwanKim.SpringToDo.domain.Task;
import HwanKim.SpringToDo.repository.MemberRepository;
import HwanKim.SpringToDo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;

    /**
     * Task 저장
     * memberId, name, desc
     * name은 member별로 unique
     */
    public void saveTask(Long memberId, Task task){
        Member member = memberRepository.findById(memberId);
        validateName(member.getId(), task.getName());
        taskRepository.save(task);
    }

    // Member별로 생성한 Task의 중복 name validate
    private void validateName(Long memberId, String name) {
        List<Task> tasks = taskRepository.findByNameInMember(name, memberId);
        if(tasks.size() != 0){
            throw new IllegalArgumentException("한 Member의 작업 목록에 같은 이름이 중복될 수 없습니다.");
        }
    }

    /**
     * Task 검색
     */

    /**
     * Task 수정
     */

    /**
     * Task 삭제
     */
}
