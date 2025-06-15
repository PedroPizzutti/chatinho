package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message create(MessageDto messageDto) {
        var message = Message.builder()
                .createdAt(messageDto.createdAt())
                .content(messageDto.content())
                .userId(messageDto.user())
                .build();
        return this.messageRepository.save(message);
    }

    public Page<Message> findAll(Pageable pageable) {
        return this.messageRepository.findAll(pageable);
    }
}
