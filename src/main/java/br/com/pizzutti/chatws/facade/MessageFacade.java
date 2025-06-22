package br.com.pizzutti.chatws.facade;

import br.com.pizzutti.chatws.dto.MessageDto;
import br.com.pizzutti.chatws.dto.MessagePageDto;
import br.com.pizzutti.chatws.model.Message;
import br.com.pizzutti.chatws.model.User;
import br.com.pizzutti.chatws.service.MessageService;
import br.com.pizzutti.chatws.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageFacade {

    private Map<Long, User> listUser;
    private final MessageService messageService;
    private final UserService userService;

    public MessageFacade(MessageService messageService,
                         UserService userService) {
        this.listUser = new HashMap<>();
        this.messageService = messageService;
        this.userService = userService;
    }

    public Message create(MessageDto messageDto) {
        var user = this.userService.findByLogin(messageDto.user());
        var message = Message.builder()
                .createdAt(messageDto.createdAt())
                .userId(user.getId())
                .content(messageDto.content())
                .build();
        return this.messageService.create(message);
    }

    public MessagePageDto findLatest(Integer page, Integer perPage) {
        var pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));
        var pageMessages = this.messageService.findAll(pageable);
        var messagesReversed = pageMessages.getContent().stream().map(this::getMessageDto).toList().reversed();
        return MessagePageDto
                .builder()
                .data(messagesReversed)
                .page(pageMessages.getNumber() + 1)
                .perPage(pageMessages.getSize())
                .totalPages(pageMessages.getTotalPages())
                .records(pageMessages.getNumberOfElements())
                .totalRecords(pageMessages.getTotalElements())
                .build();
    }

    private MessageDto getMessageDto(Message message) {
        User user = null;
        if (listUser.containsKey(message.getUserId())) {
            user = listUser.get(message.getUserId());
        } else {
            user = this.userService.findById(message.getUserId());
            this.listUser.put(user.getId(), user);
        }
        return MessageDto.builder()
                .createdAt(message.getCreatedAt())
                .content(message.getContent())
                .nick(user.getNickname())
                .user(user.getLogin())
                .build();
    }

}
