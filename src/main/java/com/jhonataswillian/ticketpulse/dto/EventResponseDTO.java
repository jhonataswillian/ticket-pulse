package com.jhonataswillian.ticketpulse.dto;

import com.jhonataswillian.ticketpulse.domain.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(UUID id, String name, int totalCapacity, LocalDateTime date) {
    public static EventResponseDTO fromEntity(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getTotalCapacity(),
                event.getDate());
    }
}
