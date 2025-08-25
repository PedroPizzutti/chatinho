package dev.pizzutti.chatinho.worker.messaging;

import dev.pizzutti.chatinho.worker.dto.MessageDto;
import dev.pizzutti.chatinho.worker.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final MessageService messageService;

    public RabbitMQListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues = "ws.messages")
    public void readMessage(MessageDto messageDto) {
        this.messageService.create(messageDto);
    }
}
