package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    Long id;

    private String name;
    private String username;
    private String password;

    /**
     * setter를 사용하지 않고, 파라미터가 있는 생성자로 최초 1회 생성하도록 제한하기 위해
     * default 생성자를 protected로 제한함
     */
    protected Member(){
    }

    public Member(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
