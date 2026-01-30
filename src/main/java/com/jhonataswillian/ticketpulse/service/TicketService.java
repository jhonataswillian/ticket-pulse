package com.jhonataswillian.ticketpulse.service;

import com.jhonataswillian.ticketpulse.domain.Ticket;
import com.jhonataswillian.ticketpulse.domain.TicketStatus;
import com.jhonataswillian.ticketpulse.repository.TicketRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StringRedisTemplate redisTemplate;

    public TicketService(TicketRepository ticketRepository, StringRedisTemplate redisTemplate) {
        this.ticketRepository = ticketRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void buyTicket(UUID ticketId) {

        String lockKey = "lock:ticket:" + ticketId;
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "LOCKED", Duration.ofMinutes(2));

        if(!lockAcquired) {
            throw new RuntimeException("Ingresso em processamento por outro usuário. Tente novamente.");
        }

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ingresso não encontrado"));

            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new RuntimeException("Ingresso indisponível (Status: " + ticket.getStatus() + ")");
            }

            ticket.setStatus(TicketStatus.SOLD);
            ticketRepository.save(ticket);
        } finally {
            redisTemplate.delete(lockKey);
        }
    }
}
