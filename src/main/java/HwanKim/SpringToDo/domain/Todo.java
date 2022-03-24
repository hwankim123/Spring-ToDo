package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDate todoDate;
    @Enumerated(EnumType.STRING)
    private TodoStatus status; // 오늘의 할일 상태 [READY, RUNNING, FINISH]

    //===연관관계 메서드===//
    public void addTodoTask(TodoTask todoTask){
        this.todoTasks.add(todoTask);
        todoTask.setTodo(this);
    }

    public static Todo create(Member member, TodoTask... todoTasks ){
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
}
