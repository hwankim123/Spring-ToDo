package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class TodoTask {

    @Id @GeneratedValue
    @Column(name = "todo_task_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(columnDefinition = "TEXT")
    private String name;
    @Column(columnDefinition = "TEXT")
    private String desc;
    @Enumerated(EnumType.STRING)
    private TodoTaskStatus status;

    //===생성 메서드===//
    public static TodoTask createTodoTask(String name, String desc){
        TodoTask todoTask = new TodoTask();
        todoTask.setName(name);
        todoTask.setDesc(desc);
        todoTask.run();
        return todoTask;
    }

    //==비즈니스 로직==//
    public void run(){
        this.status = TodoTaskStatus.RUNNING;
    }

    public void finish(){
        this.status = TodoTaskStatus.FINISH;
    }

    //===Setter===//
    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }
}
