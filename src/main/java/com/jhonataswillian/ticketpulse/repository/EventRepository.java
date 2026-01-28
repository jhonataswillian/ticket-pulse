package com.jhonataswillian.ticketpulse.repository;

import com.jhonataswillian.ticketpulse.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
