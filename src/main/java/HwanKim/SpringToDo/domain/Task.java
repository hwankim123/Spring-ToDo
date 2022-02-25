package HwanKim.SpringToDo.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Task {

    @Id @GeneratedValue
    @Column(name = "task_id")
    Long id;

    String name;

    @Column(columnDefinition = "TEXT")
    String desc;
}
