package com.jhonataswillian.ticketpulse.dto;

import java.util.UUID;

public record TicketSoldEvent(
        UUID ticketId,
        String customerEmail
) {
}
