package br.com.pizzutti.chatws.component;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.facade.MessageFacade;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListenerComponent {

    private final MessageFacade messageFacade;

    public RabbitMQListenerComponent(MessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    @RabbitListener(queues = "ws.messages")
    public void readMessage(MessageDto messageDto) {
        this.messageFacade.create(messageDto);
    }

}
