package com.psjw.coupon.domain.repository.event;

import com.psjw.coupon.domain.model.event.Event;

import java.util.Optional;

public interface EventRepository {
    Event save(Event event);

    Optional<Event> findById(Long eventId);
    boolean existsById(Long eventId);
}
