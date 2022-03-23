package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Task {

    @Id @GeneratedValue
    @Column(name = "task_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    String name;
    @Column(columnDefinition = "TEXT")
    String desc;

    protected Task(){}

    public Task(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public void upload(String name, String desc){
        if(name != null){
            this.name = name;
        }
        if(desc != null){
            this.desc = desc;
        }
    }
}
