package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class TodoTask extends BaseTimeEntity{

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

    /**
     * setter를 외부에서 사용하지 못하도록 하고, TodoTask 모델의 생성 메서드로만 TodoTask를 생성할 수 있도록 하기 위해
     * default 생성자를 protected로 제한함
     */
    protected TodoTask(){}

    //===생성 메서드===//
    public static TodoTask createTodoTask(String name, String desc){
        TodoTask todoTask = new TodoTask();
        todoTask.setName(name);
        todoTask.setDesc(desc);
        todoTask.run();
        return todoTask;
    }

    //==비즈니스 로직==//

    public void update(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    /**
     * 할일에 등록된 작업을 미완료(RUNNING) 상태로 변경
     */
    public void run(){
        this.status = TodoTaskStatus.RUNNING;
    }

    /**
     * 할일에 등록된 작업을 완료(FINISH) 상태로 변경
     */
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
