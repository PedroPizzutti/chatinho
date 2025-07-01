package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.dto.MessagePageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.service.MessageService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class MessageFacade {
    private final MessageService messageService;
    private final UserService userService;

    public MessageFacade(MessageService messageService,
                         UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    public Message create(MessageDto messageDto) {
        var message = Message.builder()
                .createdAt(messageDto.createdAt())
                .user(messageDto.user())
                .room(messageDto.room())
                .type(messageDto.type())
                .content(messageDto.content())
                .build();
        return this.messageService.create(message);
    }

    public MessagePageDto findLatest(Integer page, Integer perPage) {
        var pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));
        var pageMessages = this.messageService.findAll(pageable);
        return MessagePageDto
                .builder()
                .data(pageMessages.getContent().stream().map(this::getMessageDto).toList())
                .page(pageMessages.getNumber() + 1)
                .perPage(pageMessages.getSize())
                .totalPages(pageMessages.getTotalPages())
                .records(pageMessages.getNumberOfElements())
                .totalRecords(pageMessages.getTotalElements())
                .build();
    }

    private MessageDto getMessageDto(Message message) {
        return MessageDto.builder()
                .createdAt(message.getCreatedAt())
                .content(message.getContent())
                .room(message.getRoom())
                .user(message.getUser())
                .type(message.getType())
                .nick(this.userService.findById(message.getUser()).getNickname())
                .build();
    }
}
