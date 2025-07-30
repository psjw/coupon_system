package com.psjw.coupon.domain.model.event;

import com.psjw.coupon.common.entity.BaseAuditEntity;
import com.psjw.coupon.domain.enums.event.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    public void changeStatus(EventStatus newStatus) {
        if (cannotChangeTo(newStatus)) {
            throw new IllegalStateException("허용되지 않는 상태 변경입니다.");
        }
        this.eventStatus = newStatus;
    }

    private boolean cannotChangeTo(EventStatus newStatus) {
        if (this.eventStatus == EventStatus.FINISHED) return false;
        if (this.eventStatus == newStatus) return false;
        return true;
    }

    public void delete() {
        if (this.isDeleted) {
            throw new IllegalStateException("이미 삭제된 이벤트 입니다.");
        }
        if (cannotDelete()) {
            throw new IllegalStateException("허용되지 않는 상태 변경입니다.");
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    private boolean cannotDelete() {
        return this.eventStatus == EventStatus.READY || this.eventStatus == EventStatus.IN_PROGRESS;
    }

    @Builder(access = AccessLevel.PROTECTED)
    private Event(String eventName, String description, LocalDateTime fromAt, LocalDateTime untilAt, EventStatus eventStatus) {
        this.eventName = eventName;
        this.description = description;
        this.fromAt = fromAt;
        this.untilAt = untilAt;
        this.eventStatus = eventStatus;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static Event of(String eventName, String description, LocalDateTime fromAt, LocalDateTime untilAt) {
        return Event.builder()
                .eventName(eventName)
                .description(description)
                .fromAt(fromAt)
                .untilAt(untilAt)
                .build();
    }
}

