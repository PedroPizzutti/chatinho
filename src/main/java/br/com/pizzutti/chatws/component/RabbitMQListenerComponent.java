package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListenerComponent {

    private final MessageService messageService;

    public RabbitMQListenerComponent(MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues = "ws.messages")
    public void readMessage(MessageDto messageDto) {
        this.messageService.create(messageDto);
    }

}
