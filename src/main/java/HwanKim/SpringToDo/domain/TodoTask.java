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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private Duration taskDuration;
    @Column(columnDefinition = "TEXT")
    private String desc;

    // 연관관계 메서드를 위한 setter
    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    //===생성 메서드===//
    public static TodoTask createTodoTask(Task task, Duration taskDuration){
        TodoTask todoTask = new TodoTask();
        todoTask.setTask(task);
        todoTask.setTaskDuration(taskDuration);
        return todoTask;
    }

    private void setTaskDuration(Duration taskDuration) {
        this.taskDuration = taskDuration;
    }

    private void setTask(Task task) {
        this.task = task;
    }
}
