package com.jhonataswillian.ticketpulse.controller;

import com.jhonataswillian.ticketpulse.dto.TicketPurchaseDTO;
import com.jhonataswillian.ticketpulse.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
