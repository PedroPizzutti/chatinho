package br.com.pizzutti.chatinho.api.domain.message;

import br.com.pizzutti.chatinho.api.infra.service.FilterDirectionEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterOperationEnum;
import br.com.pizzutti.chatinho.api.infra.service.FilterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends FilterService<Message> {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        super();
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

    public Page<Message> find(Integer page, Integer perPage) {
        try {
            var pageable = PageRequest.of(page - 1, perPage, super.sort());
            return this.messageRepository.findAll(super.specification(), pageable);
        } finally {
            super.reset();
        }
    }
}
