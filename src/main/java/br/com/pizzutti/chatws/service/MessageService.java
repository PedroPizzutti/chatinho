package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                .idUser(messageDto.idUser())
                .idRoom(messageDto.idRoom())
                .type(messageDto.type())
                .content(messageDto.content())
                .build();
        return this.messageRepository.saveAndFlush(message);
    }

    public Page<Message> findLatestByIdRoom(Long idRoom, Integer page, Integer perPage) {
        var pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.messageRepository.findByIdRoom(idRoom, pageable);
    }
}
