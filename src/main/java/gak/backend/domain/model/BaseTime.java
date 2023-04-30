package gak.backend.domain.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass //Entity가 BaseTime을 상속 받을 때, 생성 시간, 수정 시간을 인식하게 함.
@EntityListeners(AuditingEntityListener.class) //자동으로 값을 넣어줌.
@Getter
public abstract class BaseTime {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
