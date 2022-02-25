package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Todo {
    @Id @GeneratedValue
    @Column(name = "todo_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "todo")
    private List<TodoTask> todoTasks = new ArrayList<>();

    private LocalDateTime todoDate;

    @Enumerated(EnumType.STRING)
    private TodoStatus status; // 오늘의 할일 상태 [READY, RUNNING, FINISH]
}
