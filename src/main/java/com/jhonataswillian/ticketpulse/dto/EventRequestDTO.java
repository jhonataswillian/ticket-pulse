package com.jhonataswillian.ticketpulse.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Min(value = 10, message = "Capacidade mínima de 10 pessoas")
        int totalCapacity,

        @NotNull(message = "A data é obrigatória")
        @Future(message = "O evento deve ser no futuro")
        LocalDateTime date,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price
) {
}
