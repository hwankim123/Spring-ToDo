package HwanKim.SpringToDo.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA 엔티티 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하게 해줌
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity에 JPA Auditing 기능을 추가시킴
public abstract class BaseTimeEntity {

    @CreatedDate // Entity가 생성되어 저장될때 자동으로 생성 시간이 저장됨
    private LocalDateTime createDate;

    @LastModifiedDate // 조회한 Entity값을 변경할 때 자동으로 수정 시간이 저장됨
    private LocalDateTime modifiedDate;
}
