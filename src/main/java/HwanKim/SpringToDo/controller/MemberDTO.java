package HwanKim.SpringToDo.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {
    private Long id;
    private String name;
    private String username;
    private String password;

    public MemberDTO(Long id, String name, String username, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
