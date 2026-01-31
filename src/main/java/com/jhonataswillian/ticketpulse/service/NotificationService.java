package com.jhonataswillian.ticketpulse.service;

import com.jhonataswillian.ticketpulse.dto.TicketSoldEvent;
import com.jhonataswillian.ticketpulse.infra.queue.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @RabbitListener(queues = RabbitConfig.QUEUE_SALES_CONFIRMATION)
    public void handleSale(TicketSoldEvent event) {
        // Simular processamento pesado
        System.out.println("------------------------------------------------");
        System.out.println("E-MAIL RECEBIDO NA FILA: Processando venda do Ticket: " + event.ticketId());
        System.out.println("Enviando e-mail para: " + event.customerEmail());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Notificação enviada com sucesso!");
        System.out.println("------------------------------------------------");
    }
}
