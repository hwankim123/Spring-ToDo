package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    Long id;

    String name;

    String username;
    String password;

    protected Member(){
    }

    public Member(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
