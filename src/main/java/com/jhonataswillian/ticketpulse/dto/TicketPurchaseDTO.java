package com.jhonataswillian.ticketpulse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TicketPurchaseDTO(

        @NotNull
        UUID ticketId,

        @NotNull
        String cardToken,

        @NotNull
        @Email
        String email
) {
}
