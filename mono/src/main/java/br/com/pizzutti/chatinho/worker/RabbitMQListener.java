package br.com.pizzutti.chatinho.worker;

import br.com.pizzutti.chatinho.api.domain.message.MessageDto;
import br.com.pizzutti.chatinho.api.domain.message.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQListener(MessageService messageService,
                            RabbitTemplate rabbitTemplate1) {
        this.messageService = messageService;
        this.rabbitTemplate = rabbitTemplate1;
    }

    @RabbitListener(queues = "ws.messages")
    public void readMessage(MessageDto messageDto) {
        try {
            this.messageService.create(messageDto);
        } catch (Exception e) {
            this.rabbitTemplate.setRoutingKey("ws.errors");
            this.rabbitTemplate.convertAndSend(messageDto);
        }
    }
}
