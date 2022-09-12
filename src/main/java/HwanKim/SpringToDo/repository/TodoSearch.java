package HwanKim.SpringToDo.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class TodoSearch {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
}
