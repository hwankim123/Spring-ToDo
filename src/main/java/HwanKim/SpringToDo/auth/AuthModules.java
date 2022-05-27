package HwanKim.SpringToDo.auth;

import HwanKim.SpringToDo.DTO.TaskDTO;
import HwanKim.SpringToDo.exception.SessionInvalidException;
import HwanKim.SpringToDo.exception.WrongDataAccessException;
import HwanKim.SpringToDo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthModules {

    public final TaskService taskService;

    /**
     * Session에 user id가 없으면 throw Exception.
     */
    public void checkSession(HttpSession session){
        if(session.getAttribute(SessionStrings.SESSION_ID) == null){
            throw new SessionInvalidException("로그인 정보가 없습니다. 로그인 후 서비스를 이용해주시기 바랍니다.");
        }
    }

    /**
     * Task Entity에 대한 접근 권한 검사
     * url에 담겨온 task id의 외래키인 memberId가 session에 저장된 memberId와 일치하는지 검사
     */
    public void checkAuthofTask(Long memberId, Long taskId, CRUDStatus curdStatus) {
        Optional<TaskDTO> task = taskService.findAll(memberId).stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst();
        if(task.isEmpty()){
            if(curdStatus.equals(CRUDStatus.UPDATE)){
                throw new WrongDataAccessException("수정 권한이 없는 작업에 대한 수정 요청입니다.");
            } else if(curdStatus.equals(CRUDStatus.DELETE)){
                throw new WrongDataAccessException("삭제 권한이 없는 작업에 대한 삭제 요청입니다.");
            }
        }
    }

}
