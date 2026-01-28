package com.jhonataswillian.ticketpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequestDTO(
        String name,
        int totalCapacity,
        LocalDateTime date,
        BigDecimal price
) {
}
