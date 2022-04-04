package HwanKim.SpringToDo.repository;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class TodoSearch {
    private Long memberId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
