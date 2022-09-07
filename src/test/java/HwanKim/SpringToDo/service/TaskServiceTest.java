//package HwanKim.SpringToDo.service;
//
//import HwanKim.SpringToDo.DTO.MemberDTO;
//import HwanKim.SpringToDo.DTO.TaskDto;
//import HwanKim.SpringToDo.domain.Member;
//import HwanKim.SpringToDo.domain.Task;
//import HwanKim.SpringToDo.exception.TaskNameDuplicateException;
//import HwanKim.SpringToDo.repository.MemberRepository;
//import HwanKim.SpringToDo.repository.TaskRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class TaskServiceTest {
//
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    TaskRepository taskRepository;
//    @Autowired
//    TaskService taskService;
//
//    @Test
//    public void Task저장() throws Exception{
//        //given
//        Member member = new Member("김환", "hwankim123", "cjsak123!");
//        MemberDTO memberDTO = new MemberDTO(member.getId(), member.getName(), member.getUsername(), member.getPassword());
//        Long memberId = memberService.signUp(memberDTO);
//
//        //when
//        TaskDto taskDTO = new TaskDto(member, "백준", "DP");
//        Long taskId = taskService.saveTask(taskDTO);
//        Task task = taskRepository.findById(memberId, taskId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
//        //then
//        System.out.println("taskId = " + taskId + " task.getId() = " + task.getId());
//        assertThat(taskId).isEqualTo(task.getId());
//    }
//
////    @Test
////    public void 중복_이름_예외_Task저장(){
////        //given
////        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123!");
////        Long memberId = memberService.signUp(memberDTO);
////
////
////        //when
////        TaskDto taskDTO = new TaskDto(memberRepository.findById(memberId), "백준", "DP");
////        Long task1_id = taskService.saveTask(taskDTO);
////        Task foundTask1 = taskRepository.findById(memberId, task1_id)
////                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
////        System.out.println("foundTask1 = " + foundTask1.getMember().getName());
////
////        //then
////        assertThrows(TaskNameDuplicateException.class, () -> {
////            TaskDto taskDto2 = new TaskDto(memberRepository.findById(memberId), "백준", "오잉");
////            taskService.saveTask(taskDto2);
////        });
////    }
//
//    @Test
//    public void 전체_Task조회() throws Exception{
//        //given
//        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123!");
//        Long memberId = memberService.signUp(memberDTO);
//
//        Member member = memberRepository.findById(memberId);
//        TaskDto taskDto1 = new TaskDto(member, "백준1", "DP");
//        TaskDto taskDto2 = new TaskDto(member, "백준2", "DP");
//        TaskDto taskDto3 = new TaskDto(member, "백준3", "DP");
//        Long task1_id = taskService.saveTask(taskDto1);
//        Long task2_id = taskService.saveTask(taskDto2);
//        Long task3_id = taskService.saveTask(taskDto3);
//
//        //when
//        List<TaskDto> tasks = taskService.findAll(memberId);
//
//        //then
//        for(TaskDto task : tasks) {
//            System.out.println("tasks = " + task);
//        }
//        assertThat(tasks.size()).isEqualTo(3);
//    }
//
//    //find One
//    @Test
//    public void 이름_Task조회(){
//        //given
//        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123");
//        Long memberId = memberService.signUp(memberDTO);
//
//        Member member = memberRepository.findById(memberId);
//        System.out.println("member.getName() = " + member.getName());
//        TaskDto taskDto1 = new TaskDto(member, "백준1", "DP");
//        TaskDto taskDto2 = new TaskDto(member, "백준2", "DP");
//        TaskDto taskDto3 = new TaskDto(member, "백준3", "DP");
//        Long task1Id = taskService.saveTask(taskDto1);
//        Long task2Id = taskService.saveTask(taskDto2);
//        Long task3Id = taskService.saveTask(taskDto3);
//        Task task1 = taskRepository.findById(memberId, task1Id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 Task가 존재하지 않습니다."));
//        System.out.println("task1.getId() = " + task1.getId());
//
//        //when
//        TaskDto findTasks = taskService.findOneByName(memberId, "백준1");
//
//        //then
//        assertThat(findTasks.getId()).isEqualTo(task1.getId());
//
//    }
//
//    //update - 중복 이름 예외(수정하고자 하는 task가 다른 task의 이름과 겹칠 때) : 예외
//    @Test
//    public void 중복_이름_예외_수정(){
//        //given
//        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123");
//        Long memberId = memberService.signUp(memberDTO);
//
//        Member member = memberRepository.findById(memberId);
//        TaskDto taskDto1 = new TaskDto(member, "task1", "task1입니다.");
//        TaskDto taskDto2 = new TaskDto(member, "task2", "task2입니다.");
//        Long task1Id = taskService.saveTask(taskDto1);
//        Long task2Id = taskService.saveTask(taskDto2);
//
//        //when
//        TaskDto updatedTaskDto = TaskDto.builder()
//                .id(task1Id)
//                .name("task2")
//                .desc("task2로 바꿔볼까?")
//                .build();
//        //then
//        assertThrows(TaskNameDuplicateException.class, () -> {
//            taskService.update(memberId, updatedTaskDto);
//        });
//    }
//
//    @Test
//    public void 이름_수정_안할때_정상_수정(){
//        //given
//        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123");
//        Long memberId = memberService.signUp(memberDTO);
//
//        Member member = memberRepository.findById(memberId);
//        TaskDto taskDto1 = new TaskDto(member, "task1", "task1입니다.");
//        TaskDto taskDto2 = new TaskDto(member, "task2", "task2입니다.");
//        Long task1Id = taskService.saveTask(taskDto1);
//        Long task2Id = taskService.saveTask(taskDto2);
//
//        //when
//        TaskDto updatedTaskDto = TaskDto.builder()
//                .id(task1Id)
//                .name("task1")
//                .desc("이름은 그대로 냅둬볼까?")
//                .build();
//        taskService.update(memberId, updatedTaskDto);
//
//        //then
//        assertThat(task1Id).isEqualTo(taskRepository.findByName(updatedTaskDto.getName()).get(0).getId());
//        assertThat(taskDto1.getName()).isEqualTo(updatedTaskDto.getName());
//        assertThat(taskDto1.getDesc()).isNotEqualTo(updatedTaskDto.getDesc());
//    }
//
//    // delete
//    @Test
//    public void 삭제(){
//        //given
//        MemberDTO memberDTO = new MemberDTO("김환", "hwankim123", "cjsak123");
//        Long memberId = memberService.signUp(memberDTO);
//
//        Member member = memberRepository.findById(memberId);
//        TaskDto taskDTO = new TaskDto(member, "task1", "task1입니다.");
//        Long task1Id = taskService.saveTask(taskDTO);
//        //when
//        taskService.delete(memberId, task1Id);
//        List<TaskDto> tasks = taskService.findAll(memberId);
//
//        //then
//        assertThat(tasks.size()).isEqualTo(0);
//    }
//}