package psjw.coupon.domain.model.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psjw.coupon.common.entity.BaseAuditEntity;
import psjw.coupon.domain.enumtype.event.EventStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseAuditEntity {
    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String eventName;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime fromAt;

    @Column(nullable = false)
    private LocalDateTime untilAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus eventStatus;


    public void changeStatus(EventStatus newStatus) {
        if (this.canChangeTo(newStatus)){
            throw new IllegalStateException("허용되지 않는 상태 변경입니다.");
        }
        this.eventStatus = newStatus;
    }

    private boolean canChangeTo(EventStatus newStatus) {
        if (this.eventStatus == EventStatus.FINISHED) return false;
        if (this.eventStatus == newStatus) return false;
        return true;
    }
}

