package com.psjw.coupon.infrastructure.persistence.jpa.event;

import com.psjw.coupon.domain.model.event.Event;
import com.psjw.coupon.domain.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaEventRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public Event save(Event event) {
        return eventJpaRepository.save(event);
    }

    @Override
    public Optional<Event> findById(Long eventId) {
        return eventJpaRepository.findById(eventId);
    }

    @Override
    public boolean existsById(Long eventId) {
        return eventJpaRepository.existsById(eventId);
    }
}
