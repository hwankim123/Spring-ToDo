package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Task extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "task_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    String name;
    @Column(columnDefinition = "TEXT")
    String desc;

    /**
     * setter를 외부에서 사용하지 못하도록 하고, Task 모델의 생성 메서드로만 Task를 생성할 수 있도록 하기 위해
     * default 생성자를 protected로 제한함
     */
    protected Task(){}

    //===생성 메서드===//
    public static Task create(Member member, String name, String desc){
        Task task = new Task();
        task.setMember(member);
        task.setName(name);
        task.setDesc(desc);
        return task;
    }

    // setter을 public으로 개방하지 않고 필요한 비즈니스 로직, 생성 메서드 내부에서만 쓰이도록 private로 선언
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
        this.name = name;
        this.desc = desc;
    }
}
