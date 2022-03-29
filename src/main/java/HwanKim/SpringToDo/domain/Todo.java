package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
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

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<TodoTask> todoTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TodoStatus status; // 오늘의 할일 상태 [READY, RUNNING, PAUSE, FINISH]
    private Duration totalDuration;
    private LocalDateTime startTime;
    private LocalDateTime restartTime;
    private LocalDateTime finishTime;

    //===연관관계 메서드===//
    public void addTodoTask(TodoTask todoTask){
        this.todoTasks.add(todoTask);
        todoTask.setTodo(this);
    }

    public static Todo create(Member member, List<TodoTask> todoTasks ){
        Todo todo = new Todo();
        todo.setMember(member);
        for(TodoTask todoTask : todoTasks){
            todo.addTodoTask(todoTask);
        }
        todo.setStatus(TodoStatus.READY);
        return todo;
    }

    private void setStatus(TodoStatus todoStatus) {
        this.status = todoStatus;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    //==비즈니스 로직==//
    public void start(){
        this.setStatus(TodoStatus.RUNNING);
        if(this.startTime == null){
            this.startTime = LocalDateTime.now();
        } else{
            this.restartTime = LocalDateTime.now();
        }
    }

    public void pause(){
        this.setStatus(TodoStatus.PAUSE);

    }
}
