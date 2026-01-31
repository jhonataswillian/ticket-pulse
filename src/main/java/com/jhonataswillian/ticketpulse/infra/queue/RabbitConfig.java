package com.jhonataswillian.ticketpulse.infra.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_SALES_CONFIRMATION = "ticket-sales-confirmation";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_SALES_CONFIRMATION, true);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new TicketPulseConverter(objectMapper);
    }
}
