package com.jhonataswillian.ticketpulse.service;

import com.jhonataswillian.ticketpulse.domain.Event;
import com.jhonataswillian.ticketpulse.domain.Ticket;
import com.jhonataswillian.ticketpulse.domain.TicketStatus;
import com.jhonataswillian.ticketpulse.dto.EventRequestDTO;
import com.jhonataswillian.ticketpulse.dto.EventResponseDTO;
import com.jhonataswillian.ticketpulse.repository.EventRepository;
import com.jhonataswillian.ticketpulse.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public EventService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public EventResponseDTO save(EventRequestDTO dto) {

        Event event = new Event();
        event.setName(dto.name());
        event.setTotalCapacity(dto.totalCapacity());
        event.setDate(dto.date());
        event = eventRepository.save(event);

        List<Ticket> tickets = new ArrayList<>(event.getTotalCapacity());
        for (int i = 0; i < event.getTotalCapacity(); i++) {

            Ticket ticket = new Ticket();
            ticket.setPrice(dto.price());
            ticket.setStatus(TicketStatus.AVAILABLE);
            ticket.setEvent(event);
            tickets.add(ticket);
        }
        ticketRepository.saveAll(tickets);

        return EventResponseDTO.fromEntity(event);
    }
}
