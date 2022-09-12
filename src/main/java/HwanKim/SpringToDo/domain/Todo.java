package HwanKim.SpringToDo.domain;

import HwanKim.SpringToDo.controller.Todo.TodoForm;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Todo extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Todo에서 TodoTask를 관리. cascade all 옵션과 orphanRemoval=true로 설정함. 이래야 Todo.todoTasks에서 삭제했을 때 삭제 쿼리가 나감
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoTask> todoTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TodoTaskStatus status;
    private LocalDate createdDate;

    /**
     * setter를 외부에서 사용하지 못하도록 하고, Todo 모델의 생성 메서드로만 Todo를 생성할 수 있도록 하기 위해
     * default 생성자를 protected로 제한함
     */
    protected Todo(){}

    //===연관관계 메서드===//
    public void addTodoTask(TodoTask todoTask) {
        this.todoTasks.add(todoTask);
        todoTask.setTodo(this);
    }

    //===생성 메서드===//
    public static Todo create(User user, List<TodoTask> todoTasks) { // member -> User로 수정
        Todo todo = new Todo();
//        todo.setMember(member);
        todo.setUser(user);
        for (TodoTask todoTask : todoTasks) {
            todo.addTodoTask(todoTask);
        }
        todo.setCreatedDate();
        todo.run();
        return todo;
    }

    public static void update(Todo todo, TodoForm todoForm){
        List<TodoTask> todoTasks = todo.getTodoTasks();
        boolean[] isUpdated = new boolean[todoTasks.size()];
        Long[] idList = todoForm.getIds();
        String[] names = todoForm.getNames();
        String[] descs = todoForm.getDescs();

        for(int i = 0; i < idList.length; i++){
            if(idList[i] == -1){
                todoTasks.add(TodoTask.createTodoTask(names[i], descs[i]));
            } else{
                for(int j = 0; j < todoTasks.size(); j++){
                    TodoTask todoTask = todoTasks.get(j);
                    if(todoTask.getId().equals(idList[i])){
                        todoTask.update(names[i], descs[i]);
                        isUpdated[j] = true;
                    }
                }
            }
        }
        for(int i = 0; i < isUpdated.length; i++){
            if(!isUpdated[i]){
                todoTasks.remove(todoTasks.get(i));
            }
        }

        todo.mappingTodoTasks();
    }

    //===비즈니스 로직===//
    public void mappingTodoTasks() {
        for(TodoTask todoTask : todoTasks){
            if(todoTask.getTodo() == null){
                todoTask.setTodo(this);
            }
        }
    }

    /**
     * 할일의 상태를 미완료(RUNNING) 상태로 설정
     */
    public void run() {
        this.status = TodoTaskStatus.RUNNING;
    }

    /**
     * 할일에 등록된 작업의 상태를 변경(완료->미완료, 미완료->완료)
     */
    public void changeStatusOfTodoTask(Long todoTaskId, TodoTaskStatus status) {
        for(TodoTask todoTask : this.todoTasks){
            if(todoTask.getId().equals(todoTaskId)){
                if(status.equals(TodoTaskStatus.RUNNING)){
                    // 요청을 보낸 todoTask.status가 RUNNING일 경우
                    todoTask.finish();
                } else{
                    // 요청을 보낸 todoTask.status가 FINISH일 경우
                    todoTask.run();
                }
            }
        }
    }

    /**
     * 할일에 등록된 모든 작업들이 완료되었는지 check. 하나라도 미완료 시 return false
     */
    public boolean checkAllFinished() {
        for (TodoTask todoTask : this.todoTasks) {
            if (todoTask.getStatus().equals(TodoTaskStatus.RUNNING)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 할일의 상태를 완료(FINISH) 상태로 설정
     */
    public void finish() {
        this.status = TodoTaskStatus.FINISH;
    }

    private void setUser(User user){
        this.user = user;
    }

    private void setCreatedDate() {
        this.createdDate = LocalDate.now();
    }

}
