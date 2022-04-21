package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    Long id;

    private String name;
    private String username;
    private String password;

    protected Member(){
    }

    public Member(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
