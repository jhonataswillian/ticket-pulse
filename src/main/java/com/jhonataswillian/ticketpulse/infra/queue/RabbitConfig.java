package com.jhonataswillian.ticketpulse.infra.queue;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_SALES_CONFIRMATION = "ticket-sales-confirmation";
    public static final String QUEUE_SALES_DLQ = "ticket-sales-confirmation-dlq";
    public static final String DLX_NAME = "ticket-sales-dlx";

    @Bean
    public Queue salesDlq() {
        return QueueBuilder.durable(QUEUE_SALES_DLQ).build();
    }

    @Bean
    public DirectExchange salesDlx() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(salesDlq()).to(salesDlx()).with(QUEUE_SALES_CONFIRMATION);
    }

    @Bean
    public Queue salesQueue() {
        return QueueBuilder.durable(QUEUE_SALES_CONFIRMATION)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", QUEUE_SALES_CONFIRMATION)
                .build();
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new TicketPulseConverter(objectMapper);
    }
}
