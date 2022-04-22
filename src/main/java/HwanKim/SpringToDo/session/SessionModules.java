package HwanKim.SpringToDo.session;

import HwanKim.SpringToDo.exception.SessionInvalidException;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionModules {
    /**
     * Session에 user id가 없으면 throw Exception.
     * message에 담긴 string을 이름으로 하는 attribute를 model에 add
     */
    public static void checkSession(HttpSession session){
        if(session.getAttribute(SessionStrings.SESSION_ID) == null){
            throw new SessionInvalidException("sessionInvalid");
        }
    }
}
