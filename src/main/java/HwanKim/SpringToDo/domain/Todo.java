package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Todo {
    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL)
    private List<TodoTask> todoTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TodoTaskStatus status;
    @Column(unique = true)
    private LocalDate createdDate;

    //===연관관계 메서드===//
    public void addTodoTask(TodoTask todoTask) {
        this.todoTasks.add(todoTask);
        todoTask.setTodo(this);
    }

    //===생성 메서드===//
    public static Todo create(Member member, List<TodoTask> todoTasks) {
        Todo todo = new Todo();
        todo.setMember(member);
        for (TodoTask todoTask : todoTasks) {
            todo.addTodoTask(todoTask);
        }
        todo.setCreatedDate();
        todo.run();
        return todo;
    }

    //===비즈니스 로직===//
    public void run() {
        this.status = TodoTaskStatus.RUNNING;
    }

    public boolean checkAllFinished() {
        for (TodoTask todoTask : this.todoTasks) {
            if (todoTask.getStatus().equals(TodoTaskStatus.RUNNING)) {
                return false;
            }
        }
        return true;
    }

    public void finish() {
        this.status = TodoTaskStatus.FINISH;
    }

    //=== Setter ===//
    private void setMember(Member member) {
        this.member = member;
    }

    private void setCreatedDate() {
        this.createdDate = LocalDate.now();
    }
}
