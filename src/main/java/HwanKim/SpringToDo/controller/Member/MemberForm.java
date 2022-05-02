package HwanKim.SpringToDo.controller.Member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자리 입니다.")
    private String password;

}
