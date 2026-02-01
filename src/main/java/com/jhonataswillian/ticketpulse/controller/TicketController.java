package com.jhonataswillian.ticketpulse.controller;

import com.jhonataswillian.ticketpulse.dto.TicketPurchaseDTO;
import com.jhonataswillian.ticketpulse.dto.TicketResponseDTO;
import com.jhonataswillian.ticketpulse.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> buyTicket(@RequestBody @Valid TicketPurchaseDTO dto) {
        ticketService.buyTicket(dto);
        return ResponseEntity.ok("Compra realizada com sucesso! Você receberá um e-mail em breve.");
    }

    @GetMapping("/event/{eventId}/available")
    public ResponseEntity<List<TicketResponseDTO>> getAvailableTickets(@PathVariable UUID eventId) {
        var tickets = ticketService.findAvailableByEventId(eventId);
        return ResponseEntity.ok(tickets);
    }
}
