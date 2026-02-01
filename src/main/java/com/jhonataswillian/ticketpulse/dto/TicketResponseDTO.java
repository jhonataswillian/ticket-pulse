package com.jhonataswillian.ticketpulse.dto;

import com.jhonataswillian.ticketpulse.domain.Ticket;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketResponseDTO(UUID id, BigDecimal price) {
    public static TicketResponseDTO fromEntity(Ticket ticket) {
        return new TicketResponseDTO(ticket.getId(), ticket.getPrice());
    }
}
