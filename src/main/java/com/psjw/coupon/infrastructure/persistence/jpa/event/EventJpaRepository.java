package com.psjw.coupon.infrastructure.persistence.jpa.event;

import com.psjw.coupon.domain.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<Event, Long> {
}
