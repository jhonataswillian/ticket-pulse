package com.jhonataswillian.ticketpulse.repository;

import com.jhonataswillian.ticketpulse.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
