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
}
