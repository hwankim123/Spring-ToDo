//package HwanKim.SpringToDo.service;
//
//import HwanKim.SpringToDo.DTO.MemberDTO;
//import HwanKim.SpringToDo.DTO.TaskDto;
//import HwanKim.SpringToDo.DTO.TodoDto;
//import HwanKim.SpringToDo.domain.Member;
//import HwanKim.SpringToDo.domain.Todo;
//import HwanKim.SpringToDo.repository.MemberRepository;
//import HwanKim.SpringToDo.repository.TodoRepository;
//import HwanKim.SpringToDo.repository.TodoSearch;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class TodoServiceTest {
//
//    @Autowired
//    private TodoService todoService;
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private TodoRepository todoRepository;
//
//    @Test
//    public void todo_저장_조회() throws Exception{
//        //given
//        Member member1 = setMember("김환", "hwankim123", "aefewfws");
//        Member member2 = setMember("김환123", "hwankim123123", "aefewfws");
//        Member member3 = setMember("김환324", "hwankim342123", "aefewfws");
//        Long memberId1 = member1.getId();
//        Long memberId2 = member2.getId();
//        Long memberId3 = member3.getId();
//        List<Long> taskIdList1 = setTaskId123(member1);
//        List<Long> taskIdList2 = setTaskId12(member2);
//        List<Long> taskIdList3 = setTaskId23(member3);
//        //when
//        String[] names= {"백준", "Spring", "캡디"};
//        String[] descs= {"백준 1문제", "Spring 프로젝트 개발", "캡스톤"};
//        todoService.saveTodo(memberId1, names, descs);
//        todoService.saveTodo(memberId2, names, descs);
//        todoService.saveTodo(memberId3, names, descs);
//
//        //then : 테스트 코드 재작성 필요
////        List<Todo> todoList1 = todoRepository.findAllByMemberId(memberId1);
////        for(Todo todo : todoList1){
////            List<TodoTask> todoTasks = todo.getTodoTasks();
////            int i = 0;
////            for(TodoTask todoTask : todoTasks){
////                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList1.get(i));
////                i++;
////            }
////        }
////        List<Todo> todoList2 = todoRepository.findAllByMemberId(memberId2);
////        for(Todo todo : todoList2){
////            List<TodoTask> todoTasks = todo.getTodoTasks();
////            int i = 0;
////            for(TodoTask todoTask : todoTasks){
////                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList2.get(i));
////                i++;
////            }
////        }
////        List<Todo> todoList3 = todoRepository.findAllByMemberId(memberId3);
////        for(Todo todo : todoList3){
////            List<TodoTask> todoTasks = todo.getTodoTasks();
////            int i = 0;
////            for(TodoTask todoTask : todoTasks){
////                Assertions.assertThat(todoTask.getTask().getId()).isEqualTo(taskIdList3.get(i));
////                i++;
////            }
////        }
//    }
//
//    private List<String> setDescList(String ...descs) {
//        List<String> descList = new ArrayList<>();
//        for(String desc : descs){
//            descList.add(desc);
//        }
//        return descList;
//    }
//
//    @Test
//    public void todo_조회_예외(){
//        //given
//        Member member = setMember("김환", "hwankim123", "aefewfws");
//        Long memberId = member.getId();
//        List<Long> taskIdList = setTaskId123(member);
//
//        //when
//
//        String[] names= {"백준", "Spring", "캡디"};
//        String[] descs= {"백준 1문제", "Spring 프로젝트 개발", "캡스톤"};
//        Long todoId = todoService.saveTodo(memberId, names, descs);
//        TodoSearch todoSearch = new TodoSearch();
//
//        // 이 셋중에 테스트하고싶은걸 주석처리
//        todoSearch.setMemberId(memberId);
//        //todoSearch.setStartDate(LocalDateTime.of(2022, 4, 3, 12, 32,22,3333));
////        todoSearch.setEndDate(LocalDateTime.of(2022, 11, 12, 12, 32,22,3333));
//
//        //then
//        assertThrows(IllegalArgumentException.class, () -> {
//            todoService.searchTodo(todoSearch);
//        });
//    }
//
//    @Test
//    public void 오늘_생성된_todo_조회(){
//        //given
//        Member member = setMember("테스트", "test123", "aefewfws");
//        Long memberId = member.getId();
//
//        //when
//
//        String[] names= {"백준", "Spring", "캡디"};
//        String[] descs= {"백준 1문제", "Spring 프로젝트 개발", "캡스톤"};
//        Long todoId = todoService.saveTodo(memberId, names, descs);
//
//        //then
//        List<Todo> todaysTodo = todoRepository.findTodayByMemberId(memberId);
//        Assertions.assertThat(todaysTodo.get(0).getId()).isEqualTo(todoId);
//    }
//
//    @Test
//    public void 생성후_삭제후_조회_todo_조회(){
//        //given
//        Member member = setMember("테스트", "test123", "aefewfws");
//        Long memberId = member.getId();
//
//        //when
//
//        String[] names= {"백준", "Spring", "캡디"};
//        String[] descs= {"백준 1문제", "Spring 프로젝트 개발", "캡스톤"};
//        Long todoId = todoService.saveTodo(memberId, names, descs);
//
//        todoService.delete(memberId);
//        List<Todo> afterDeleteTodo = todoRepository.findTodayByMemberId(member.getId());
//
//        //then
//        Assertions.assertThat(afterDeleteTodo.size()).isEqualTo(0);
//    }
//
//    @Test
//    public void todo_삭제_테스트(){
//        //given
//        Member member = setMember("test", "test", "test123!");
//
//        String[] names= {"task1", "task2", "task3"};
//        String[] descs= {"tast1입니다.", "task22입니다.", "task333입니다."};
//        Long todoId = todoService.saveTodo(member.getId(), names, descs);
//
//        //when
//        todoService.delete(member.getId());
//
//        //then
//        Assertions.assertThat(todoRepository.findTodayByMemberId(member.getId())).isEqualTo(null);
//    }
//
//    @Test
//    public void search(){
//        Member member = setMember("test", "test", "test123!");
//
//        String[] names= {"task1", "task2", "task3"};
//        String[] descs= {"tast1입니다.", "task22입니다.", "task333입니다."};
//        Long todoId = todoService.saveTodo(member.getId(), names, descs);
//
//        TodoSearch todoSearch = new TodoSearch();
//        todoSearch.setMemberId(member.getId());
//        todoSearch.setStartDate(LocalDate.of(2020, 10, 10));
//        todoSearch.setEndDate(LocalDate.now());
//        List<TodoDto> todoDTOS = todoService.searchTodo(todoSearch);
//
//    }
//
//    private Member setMember(String name, String username, String password){
//        Member member = new Member(name, username, password);
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//        memberService.signUp(memberDTO);
//        return memberRepository.findByUsername(username).get(0);
//    }
//
//    private List<Long> setTaskId123(Member member){
//        TaskDto taskDto1 = new TaskDto(member, "task1", "task1입니다");
//        TaskDto taskDto2 = new TaskDto(member, "task2", "task2입니다");
//        TaskDto taskDto3 = new TaskDto(member, "task3", "task3입니다");
//        Long task1Id = taskService.saveTask(taskDto1);
//        Long task2Id = taskService.saveTask(taskDto2);
//        Long task3Id = taskService.saveTask(taskDto3);
//
//        List<Long> taskIdList = new ArrayList<>();
//        taskIdList.add(task1Id);
//        taskIdList.add(task2Id);
//        taskIdList.add(task3Id);
//        return taskIdList;
//    }
//
//    private List<Long> setTaskId23(Member member) {
//        TaskDto taskDto2 = new TaskDto(member, "task2", "task2입니다");
//        TaskDto taskDto3 = new TaskDto(member, "task3", "task3입니다");
//        Long task2Id = taskService.saveTask(taskDto2);
//        Long task3Id = taskService.saveTask(taskDto3);
//
//        List<Long> taskIdList = new ArrayList<>();
//        taskIdList.add(task2Id);
//        taskIdList.add(task3Id);
//        return taskIdList;
//    }
//
//    private List<Long> setTaskId12(Member member) {
//        TaskDto taskDto1 = new TaskDto(member, "task1", "task1입니다");
//        TaskDto taskDto2 = new TaskDto(member, "task2", "task2입니다");
//        Long task1Id = taskService.saveTask(taskDto1);
//        Long task2Id = taskService.saveTask(taskDto2);
//
//        List<Long> taskIdList = new ArrayList<>();
//        taskIdList.add(task1Id);
//        taskIdList.add(task2Id);
//        return taskIdList;
//    }
//}