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

    //===생성 메서드===//
    public static Task create(Member member, String name, String desc){
        Task task = new Task();
        task.setMember(member);
        task.setName(name);
        task.setDesc(desc);
        return task;
    }

    private void setMember(Member member){
        this.member = member;
    }
    private void setName(String name){
        this.name = name;
    }
    private void setDesc(String desc){
        this.desc = desc;
    }

    public void update(String name, String desc){
        if(name != null){
            this.name = name;
        }
        if(desc != null){
            this.desc = desc;
        }
    }
}
