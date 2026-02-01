package com.jhonataswillian.ticketpulse.integration;

import com.jhonataswillian.ticketpulse.domain.Ticket;
import com.jhonataswillian.ticketpulse.domain.TicketStatus;
import com.jhonataswillian.ticketpulse.dto.EventRequestDTO;
import com.jhonataswillian.ticketpulse.dto.EventResponseDTO;
import com.jhonataswillian.ticketpulse.dto.TicketPurchaseDTO;
import com.jhonataswillian.ticketpulse.infra.AbstractIntegrationTest;
import com.jhonataswillian.ticketpulse.repository.TicketRepository;
import com.jhonataswillian.ticketpulse.service.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TicketSalesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private com.jhonataswillian.ticketpulse.service.TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldCompleteFullSalesFlow() {
        EventRequestDTO eventDto = new EventRequestDTO(
                "Integration Test Event",
                10,
                LocalDateTime.now().plusDays(1),
                new BigDecimal("100.00")
        );
        EventResponseDTO createdEvent = eventService.save(eventDto);

        List<Ticket> tickets = ticketRepository.findAll();
        Ticket ticketToBuy = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.AVAILABLE)
                .findFirst()
                .orElseThrow();

        TicketPurchaseDTO purchaseDTO = new TicketPurchaseDTO(
                ticketToBuy.getId(),
                "test-card-token",
                "integration@test.com"
        );

        ticketService.buyTicket(purchaseDTO);

        Ticket soldTicket = ticketRepository.findById(ticketToBuy.getId()).orElseThrow();
        Assertions.assertEquals(TicketStatus.SOLD, soldTicket.getStatus());

        System.out.println("Teste de integração passou: Ticket vendido e salvo no banco Postgres do Testcontainers.");
    }
}
