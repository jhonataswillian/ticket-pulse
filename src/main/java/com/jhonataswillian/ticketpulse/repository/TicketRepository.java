package com.jhonataswillian.ticketpulse.repository;

import com.jhonataswillian.ticketpulse.domain.Ticket;
import com.jhonataswillian.ticketpulse.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByEventIdAndStatus(UUID eventId, TicketStatus status);
}
