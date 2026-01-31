package com.jhonataswillian.ticketpulse.infra.queue;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class TicketPulseConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

    public TicketPulseConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(object);

            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
            messageProperties.setContentLength(bytes.length);
            messageProperties.setHeader("__TypeId__",object.getClass().getName());

            return new Message(bytes, messageProperties);
        } catch (Exception e) {
            throw new MessageConversionException("Erro de serialização Jackson 3", e);
        }
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            String typeId = message.getMessageProperties().getHeader("__TypeId__");
            if (typeId == null) {
                throw new MessageConversionException("Header __TypeId__ ausente");
            }

            Class<?> targetClass = Class.forName(typeId);

            return objectMapper.readValue(message.getBody(), targetClass);
        } catch (Exception e) {
            throw new MessageConversionException("Erro de deserialização Jackson 3", e);
        }
    }
}
