package com.jhonataswillian.ticketpulse.service;

import com.jhonataswillian.ticketpulse.domain.Ticket;
import com.jhonataswillian.ticketpulse.domain.TicketStatus;
import com.jhonataswillian.ticketpulse.dto.TicketPurchaseDTO;
import com.jhonataswillian.ticketpulse.dto.TicketResponseDTO;
import com.jhonataswillian.ticketpulse.dto.TicketSoldEvent;
import com.jhonataswillian.ticketpulse.infra.queue.RabbitConfig;
import com.jhonataswillian.ticketpulse.repository.TicketRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final StringRedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public TicketService(TicketRepository ticketRepository,
                         StringRedisTemplate redisTemplate,
                         RabbitTemplate rabbitTemplate) {
        this.ticketRepository = ticketRepository;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<TicketResponseDTO> findAvailableByEventId(UUID eventId) {
        return ticketRepository.findByEventIdAndStatus(eventId, TicketStatus.AVAILABLE)
                .stream()
                .map(TicketResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void buyTicket(TicketPurchaseDTO dto) {
        UUID ticketId = dto.ticketId();

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

            var event = new TicketSoldEvent(ticketId, dto.email());
            rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_SALES_CONFIRMATION, event);
        } finally {
            redisTemplate.delete(lockKey);
        }
    }
}
