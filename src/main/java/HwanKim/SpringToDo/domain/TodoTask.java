package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;

@Entity
@Getter
public class TodoTask {

    @Id @GeneratedValue
    @Column(name = "todo_task_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private Duration taskDuration;

    @Column(columnDefinition = "TEXT")
    private String desc;
}
