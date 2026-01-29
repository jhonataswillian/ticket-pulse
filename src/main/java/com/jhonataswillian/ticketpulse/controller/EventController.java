package com.jhonataswillian.ticketpulse.controller;

import com.jhonataswillian.ticketpulse.dto.EventRequestDTO;
import com.jhonataswillian.ticketpulse.dto.EventResponseDTO;
import com.jhonataswillian.ticketpulse.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> create(
            @RequestBody @Valid EventRequestDTO dto, UriComponentsBuilder uriBuilder
    ) {
        EventResponseDTO response = eventService.save(dto);

        URI uri = uriBuilder.path("/events/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> findAll() {
        List<EventResponseDTO> list = eventService.findAll();
        return ResponseEntity.ok(list);
    }
}
