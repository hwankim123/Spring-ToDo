package HwanKim.SpringToDo.controller;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String username;
    private String password;

}
