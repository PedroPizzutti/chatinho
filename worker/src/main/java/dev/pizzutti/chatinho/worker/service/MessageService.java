package dev.pizzutti.chatinho.worker.service;

import dev.pizzutti.chatinho.worker.dto.MessageDto;
import dev.pizzutti.chatinho.worker.model.Message;
import dev.pizzutti.chatinho.worker.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void create(MessageDto messageDto) {
        this.messageRepository.save(Message.fromMessageDto(messageDto));
    }

}
