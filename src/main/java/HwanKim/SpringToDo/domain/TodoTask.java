package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

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

    private TodoTaskStatus status;
    private Duration taskDuration;
    private LocalDateTime startTime;
    private LocalDateTime restartTime;
    private LocalDateTime finishTime;
    @Column(columnDefinition = "TEXT")
    private String desc;

    // 연관관계 메서드를 위한 setter
    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    //===생성 메서드===//
    public static TodoTask createTodoTask(Task task, TodoTaskStatus status){
        TodoTask todoTask = new TodoTask();
        todoTask.setTask(task);
        todoTask.setStatus(status);
        return todoTask;
    }

    private void setTaskDuration(Duration taskDuration) {
        this.taskDuration = taskDuration;
    }

    private void setTask(Task task) {
        this.task = task;
    }

    private void setStatus(TodoTaskStatus status){
        this.status = status;
    }

    //==비즈니스 로직==//
    public void start(){
        if(this.getStatus() == TodoTaskStatus.READY){
            this.startTime = LocalDateTime.now();
        } else if(this.getStatus() == TodoTaskStatus.PAUSE){
            this.restartTime = LocalDateTime.now();
        }
        this.setStatus(TodoTaskStatus.RUNNING);
    }

    public void pause(){
        if(this.getRestartTime() == null){
            calculateDuration(this.startTime);
        } else{
            calculateDuration(this.restartTime);
        }

        this.setStatus(TodoTaskStatus.PAUSE);
    }

    public void finish() {
        if(this.getRestartTime() == null){
            calculateDuration(this.startTime);
        } else{
            calculateDuration(this.restartTime);
        }

        this.setStatus(TodoTaskStatus.FINISH);
    }

    private void calculateDuration(LocalDateTime startTime){
        int duration = LocalDateTime.now().getSecond() - startTime.getSecond();
        this.setTaskDuration(this.taskDuration.plusSeconds(duration));
    }

}
