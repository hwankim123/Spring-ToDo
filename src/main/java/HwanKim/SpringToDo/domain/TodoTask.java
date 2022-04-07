package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    //===생성 메서드===//
    public static TodoTask createTodoTask(Task task, TodoTaskStatus status, String desc){
        TodoTask todoTask = new TodoTask();
        todoTask.setTask(task);
        todoTask.setStatus(status);
        todoTask.setDesc(desc);
        todoTask.setTaskDuration(Duration.between(LocalTime.of(1, 1, 1), LocalTime.of(1, 1, 1)));
        return todoTask;
    }

    //==비즈니스 로직==//
    public void start(){
        if(this.getStatus() == TodoTaskStatus.READY){
            this.startTime = LocalDateTime.now();
        } else if(this.getStatus() == TodoTaskStatus.PAUSE){
            this.restartTime = LocalDateTime.now();
        }

        if(this.todo.getStartCnt() == 0){
            this.todo.startTodo(this.startTime);
        } else{
            this.todo.plusStartCnt();
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
        this.finishTime = LocalDateTime.now();

        if(this.todo.getFinishCnt() == this.todo.getTodoTasks().size() - 1){
            this.todo.finishTodo(this.finishTime);
        } else{
            this.todo.plusFinishCnt();
        }
        this.setStatus(TodoTaskStatus.FINISH);
    }

    private void calculateDuration(LocalDateTime startTime){
        int duration = LocalDateTime.now().getSecond() - startTime.getSecond();
        this.setTaskDuration(this.taskDuration.plusSeconds(duration));
    }

    //===Setter===//
    public void setTodo(Todo todo) {
        this.todo = todo;
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

    private void setDesc(String desc) {
        this.desc = desc;
    }
}
