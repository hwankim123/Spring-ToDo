# Spring-ToDo
Spring Boot와 JPA를 활용한 To-Do Page


## 1. 프로젝트 계획 의도
> Spring Boot 개발환경과 JPA 사용에 익숙해지기 위해 간단한 To-Do Page 개발을 기획함.
> To-Do Page를 구성하는 필수 요소들을 가지고 완성된 어플리케이션을 만든 후
> 실무라면 고려해야할 것들과 추가 기능들을 추가하는 식으로 유지보수 할 예정.

## 2. 요구사항
> ### 2.1. 회원 기능
> > * 회원 가입
> > * 로그인
> ### 2.2. 할일(Task) 등록 기능
* 할일 등록
* 할일 검색(전체 검색, 이름 검색)
* 할일 수정
> ### 2.3. 오늘의 할일(Todo) 기능
> > * 오늘의 할일 작성
> > * 오늘의 할일 수정
> > * 오늘의 할일 진행
> ### 2.4. 추가 기능(구현 예정)
> > * 요일별 Todo 조회
> > * 할일 진행률
> > * 할일 수정률

## 3. 엔티티 분석
> ### 3.1. Member
> > * id : Long
> > * name : String
> > * username(unique) : String
> > * password : String
> ### 3.2. Todo
> > * id : Long
> > * member : Member
> > * todoTasks : List<TodoTask>
> > * status : TodoStatus(enum)
> > * totalDuration : Duration
> > * startTime : LocalDateTime
> > * finishTime : LocalDateTime
> ### 3.3. TodoTask
> > * id : Long
> > * task : Task
> > * todo : Todo
> > * taskDuration : Duration
> > * desc : String(Text)
> > * 할일 진행률
> ### 3.4. Task
> > * id : Long
> > * name : String
> > * desc : String(Text)
> ### 3.5. 엔티티 간 연관관계
> > * Member와 Todo는 1:N 단방향 연관관계
> > * Todo와 Task는 N:M 연관관계. 이를 TodoTask로 풀어냄
> > * Todo와 TodoTask는 1:N 양방향 연관관계
> > * TodoTask와 Task는 N:1 단방향 연관관계
