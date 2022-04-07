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
    private TodoTaskStatus status; // 오늘의 할일 상태 [READY, RUNNING, PAUSE, FINISH]
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private int startCnt;
    private int finishCnt;

    //===연관관계 메서드===//
    public void addTodoTask(TodoTask todoTask){
        this.todoTasks.add(todoTask);
        todoTask.setTodo(this);
    }

    //===생성 메서드===//
    public static Todo create(Member member, List<TodoTask> todoTasks){
        Todo todo = new Todo();
        todo.setMember(member);
        for(TodoTask todoTask : todoTasks){
            todo.addTodoTask(todoTask);
        }
        todo.setStatus(TodoTaskStatus.READY);
        todo.setStartCnt(0);
        todo.setFinishCnt(0);
        return todo;
    }

    //===비즈니스 로직===//
    public void startTodo(LocalDateTime startTime) {
        plusStartCnt();
        this.setStartTime(startTime);
    }

    public void plusStartCnt() {
        this.setStartCnt(this.getStartCnt() + 1);
    }

    public void finishTodo(LocalDateTime finishTime) {
        plusFinishCnt();
        this.setFinishTime(finishTime);
    }

    public void plusFinishCnt() {
        this.setFinishCnt(this.getFinishCnt() + 1);
    }

    //=== Setter ===//
    private void setStatus(TodoTaskStatus todoStatus) {
        this.status = todoStatus;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    private void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    private void setStartCnt(int startCnt) {
        this.startCnt = startCnt;
    }

    private void setFinishCnt(int finishCnt) {
        this.finishCnt = finishCnt;
    }

}
