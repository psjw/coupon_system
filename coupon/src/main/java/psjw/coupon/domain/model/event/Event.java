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

}
