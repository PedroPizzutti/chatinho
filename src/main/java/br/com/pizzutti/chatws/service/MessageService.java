package br.com.pizzutti.chatws.service;

import br.com.pizzutti.chatws.dto.MessageAggregateDto;
import br.com.pizzutti.chatws.enums.FilterDirectionEnum;
import br.com.pizzutti.chatws.enums.FilterOperationEnum;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends FilterService<Message> {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public <U> MessageService filter(String property, U value, FilterOperationEnum operation) {
        super.filter(property, value, operation);
        return this;
    }

    public MessageService orderBy(String property, FilterDirectionEnum direction) {
        super.orderBy(property, direction);
        return this;
    }

    public Message create(MessageAggregateDto messageAggregateDto) {
        var message = Message.builder()
                .createdAt(messageAggregateDto.createdAt())
                .idUser(messageAggregateDto.user().id())
                .idRoom(messageAggregateDto.room().id())
                .type(messageAggregateDto.type())
                .content(messageAggregateDto.content())
                .build();
        return this.messageRepository.saveAndFlush(message);
    }

    public Page<Message> find(Integer page, Integer perPage) {
        try {
            var pageable = PageRequest.of(page - 1, perPage, super.sort());
            return this.messageRepository.findAll(super.specification(), pageable);
        } finally {
            super.reset();
        }
    }
}
